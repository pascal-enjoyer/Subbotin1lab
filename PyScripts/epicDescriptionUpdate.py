import json
import os
import sys
from typing import List, Dict

import requests


def get_headers() -> Dict[str, str]:
    token = os.environ.get("GITHUB_TOKEN")
    if not token:
        print("[ERROR] GITHUB_TOKEN is not set")
        sys.exit(1)
    return {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github+json",
    }


def load_event() -> dict:
    event_path = os.environ.get("GITHUB_EVENT_PATH")
    if not event_path or not os.path.exists(event_path):
        print("[ERROR] GITHUB_EVENT_PATH is not set or file not found")
        sys.exit(1)
    with open(event_path, "r", encoding="utf-8") as f:
        return json.load(f)


def get_repo():
    repo = os.environ.get("GITHUB_REPOSITORY")
    if not repo:
        print("[ERROR] GITHUB_REPOSITORY is not set")
        sys.exit(1)
    owner, name = repo.split("/")
    return owner, name


def list_child_prs(api_url: str, owner: str, repo: str, epic_branch: str) -> List[dict]:
    """
    Находим все PR, которые мёржатся в ветку эпика (base = epic_branch).
    """
    url = f"{api_url}/repos/{owner}/{repo}/pulls"
    params = {
        "state": "all",
        "base": epic_branch,
        "per_page": 100,
    }
    headers = get_headers()
    resp = requests.get(url, headers=headers, params=params)
    if resp.status_code != 200:
        print(f"[ERROR] Cannot list child PRs: {resp.status_code} {resp.text[:200]}")
        sys.exit(1)

    prs = resp.json()
    return prs


def find_epic_pr(api_url: str, owner: str, repo: str, epic_branch: str) -> dict | None:
    """
    Находим PR самого эпика (head = epic_branch, base = dev/main и т.п.).
    Берём первый попавшийся (обычно он один).
    """
    url = f"{api_url}/repos/{owner}/{repo}/pulls"
    params = {
        "state": "open",
        "head": f"{owner}:{epic_branch}",
        "per_page": 1,
    }
    headers = get_headers()
    resp = requests.get(url, headers=headers, params=params)
    if resp.status_code != 200:
        print(f"[ERROR] Cannot find epic PR: {resp.status_code} {resp.text[:200]}")
        sys.exit(1)

    data = resp.json()
    return data[0] if data else None


def fetch_full_pr(api_url: str, owner: str, repo: str, number: int) -> dict:
    """
    Берём полную информацию о PR (нужен merged_at).
    """
    url = f"{api_url}/repos/{owner}/{repo}/pulls/{number}"
    headers = get_headers()
    resp = requests.get(url, headers=headers)
    if resp.status_code != 200:
        print(f"[ERROR] Cannot get PR #{number}: {resp.status_code} {resp.text[:200]}")
        sys.exit(1)
    return resp.json()


def build_markdown(child_prs: List[dict]) -> str:
    """
    Формируем Markdown-блок со списком задач эпика.
    """
    if not child_prs:
        return "### Epic tasks\n\n_(no feature PRs yet)_\n"

    lines = ["### Epic tasks", ""]
    for pr in child_prs:
        full = fetch_full_pr(
            api_url=os.environ.get("GITHUB_API_URL", "https://api.github.com"),
            owner=get_repo()[0],
            repo=get_repo()[1],
            number=pr["number"],
        )
        merged = full.get("merged_at") is not None
        checkbox = "x" if merged else " "
        number = full["number"]
        title = full["title"]
        html_url = full["html_url"]
        branch = full["head"]["ref"]

        # Пример строки:
        # - [x] [#21](link) feature/feature_1 – Title
        lines.append(f"- [{checkbox}] [#{number}]({html_url}) {branch} – {title}")

    lines.append("")
    return "\n".join(lines)


def update_epic_body(api_url: str, owner: str, repo: str, epic_number: int, new_block: str):
    """
    Обновляем описание PR эпика.
    Для простоты — полностью заменяем body.
    Можно усложнить и заменить только блок между маркерами.
    """
    url = f"{api_url}/repos/{owner}/{repo}/pulls/{epic_number}"
    headers = get_headers()

    payload = {"body": new_block}
    resp = requests.patch(url, headers=headers, json=payload)
    if resp.status_code != 200:
        print(f"[ERROR] Cannot update epic PR: {resp.status_code} {resp.text[:200]}")
        sys.exit(1)

    print(f"[OK] Epic PR #{epic_number} description updated.")


def main():
    event_name = os.environ.get("GITHUB_EVENT_NAME")
    if event_name != "pull_request":
        print(f"[INFO] Not a pull_request event ({event_name}), skip epic sync")
        return

    event = load_event()
    pr = event.get("pull_request")
    if not pr:
        print("[ERROR] No pull_request in event")
        sys.exit(1)

    api_url = os.environ.get("GITHUB_API_URL", "https://api.github.com")
    owner, repo = get_repo()

    head_ref = pr["head"]["ref"]   # ветка, из которой идёт PR
    base_ref = pr["base"]["ref"]   # ветка, в которую мёржим PR

    epic_branch = None
    epic_pr_number = None

    # 1) Если это сам эпик: epic/* -> dev
    if head_ref.startswith("epic/"):
        epic_branch = head_ref
        epic_pr_number = pr["number"]
        print(f"[INFO] Current PR is epic PR #{epic_pr_number} (branch {epic_branch})")

    # 2) Если это feature -> epic/*
    elif base_ref.startswith("epic/"):
        epic_branch = base_ref
        print(f"[INFO] Feature PR to epic branch {epic_branch}, searching epic PR...")

        epic_pr = find_epic_pr(api_url, owner, repo, epic_branch)
        if not epic_pr:
            print("[WARN] No epic PR found for this epic branch, skip")
            return
        epic_pr_number = epic_pr["number"]
        print(f"[INFO] Found epic PR #{epic_pr_number}")

    else:
        print("[INFO] This PR is not epic-related, skip")
        return

    # Находим все дочерние PR (feature/* -> epic/...)
    child_prs = list_child_prs(api_url, owner, repo, epic_branch)
    markdown = build_markdown(child_prs)
    update_epic_body(api_url, owner, repo, epic_pr_number, markdown)


if __name__ == "__main__":
    main()
