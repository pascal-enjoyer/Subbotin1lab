import json
import os
import sys
from typing import Tuple

import requests


# ========= HELPERS =========

def get_headers():
    token = os.environ.get("GITHUB_TOKEN")
    if not token:
        print("[WARN] GITHUB_TOKEN is not set, GitHub API calls may fail")
        return {
            "Accept": "application/vnd.github+json",
        }
    return {
        "Authorization": f"Bearer {token}",
        "Accept": "application/vnd.github+json",
    }


def get_repo() -> str | None:
    # Репозиторий:
    # В CI: GITHUB_REPOSITORY = owner/repo
    # Локально: LOCAL_REPO

    repo = os.environ.get("GITHUB_REPOSITORY")
    if repo:
        return repo

    repo = os.environ.get("LOCAL_REPO")
    if repo:
        print(f"[!] Using LOCAL_REPO = {repo}")
    return repo


def get_api_url() -> str:
    return os.environ.get("GITHUB_API_URL", "https://api.github.com")


# Участники репозитория

def list_repo_members() -> None:
    "Перечисление участников репозитория(collaborators & contributors"

    repo = get_repo()
    api_url = get_api_url()

    if not repo:
        print("[!] No repo set (GITHUB_REPOSITORY or LOCAL_REPO), skip members listing")
        return

    owner, repo_name = repo.split("/")

    headers = get_headers()

    # collaborators
    collab_url = f"{api_url}/repos/{owner}/{repo_name}/collaborators"
    collab_resp = requests.get(collab_url, headers=headers)
    if collab_resp.status_code not in (200, 201):
        print(f"[?] Cannot list collaborators: {collab_resp.status_code} {collab_resp.text[:200]}")
        collaborators = []
    else:
        collaborators = collab_resp.json()

    # contributors
    contr_url = f"{api_url}/repos/{owner}/{repo_name}/contributors"
    contr_resp = requests.get(contr_url, headers=headers)
    if contr_resp.status_code not in (200, 201):
        print(f"[?] Cannot list contributors: {contr_resp.status_code} {contr_resp.text[:200]}")
        contributors = []
    else:
        contributors = contr_resp.json()

    logins = set()

    for c in collaborators:
        login = c.get("login")
        if login:
            logins.add(login)

    for c in contributors:
        login = c.get("login")
        if login:
            logins.add(login)

    print("\nMembers:")
    if not logins:
        print("No members found.")
        return

    for login in sorted(logins):
        print(f"- {login}")


# Логика проверки размера PR

def detect_pr_type(pr: dict) -> Tuple[str, int]:
    # Определение типа PR:
    # feature/* или по умолчанию -> лимит 300 строк
    # refactor/* или [refactor]  -> лимит 400 строк
    # bugfix/*, fix/* или [bugfix] -> лимит 150 строк

    branch = pr.get("head", {}).get("ref", "").lower()
    title = pr.get("title", "").lower()

    pr_type = "feature"
    limit = 300

    if branch.startswith("refactor/") or "[refactor]" in title:
        pr_type = "refactor"
        limit = 400
    elif branch.startswith("bugfix/") or branch.startswith("fix/") or "[bugfix]" in title:
        pr_type = "bugfix"
        limit = 150

    print(f"[INFO] Detected PR type: {pr_type}, branch: {branch}, title: {pr.get('title')}")
    return pr_type, limit


def check_pr_size_ci() -> None:
    # Вариант для CI:
    # GITHUB_EVENT_NAME = pull_request
    # GITHUB_EVENT_PATH указывает на payload

    event_name = os.environ.get("GITHUB_EVENT_NAME")
    if event_name != "pull_request":
        print(f"\n[!] Not a pull_request event ({event_name}), skipping PR size check (CI mode)")
        return

    event_path = os.environ.get("GITHUB_EVENT_PATH")
    if not event_path or not os.path.exists(event_path):
        print("[ERROR] GITHUB_EVENT_PATH is not set or file does not exist")
        sys.exit(1)

    with open(event_path, "r", encoding="utf-8") as f:
        event = json.load(f)

    pr = event.get("pull_request")
    if not pr:
        print("[ERROR] No pull_request field in event payload")
        sys.exit(1)

    repo = get_repo()
    api_url = get_api_url()
    if not repo:
        print("[ERROR] Repo is not set (GITHUB_REPOSITORY)")
        sys.exit(1)

    owner, repo_name = repo.split("/")

    pr_number = pr.get("number")
    headers = get_headers()

    pr_url = f"{api_url}/repos/{owner}/{repo_name}/pulls/{pr_number}"
    resp = requests.get(pr_url, headers=headers)
    if resp.status_code != 200:
        print(
            f"[ERROR] Cannot get PR info: status={resp.status_code}, body={resp.text[:200]}"
        )
        sys.exit(1)

    pr_data = resp.json()
    _print_and_validate_pr(pr_data, pr_number)


def check_pr_size_local() -> None:
    # Локальный вариант в venv:
    # export GITHUB_TOKEN="-"
    # export LOCAL_REPO="-"
    # export LOCAL_PR_NUMBER="-"

    repo = get_repo()
    api_url = get_api_url()
    local_pr = os.environ.get("LOCAL_PR_NUMBER")

    if not repo or not local_pr:
        print("[!] LOCAL_PR_NUMBER or repo not set, skip local PR size check")
        return

    print(f"[!] Local debug: repo={repo}, PR={local_pr}")

    owner, repo_name = repo.split("/")
    headers = get_headers()

    pr_url = f"{api_url}/repos/{owner}/{repo_name}/pulls/{local_pr}"
    resp = requests.get(pr_url, headers=headers)
    if resp.status_code != 200:
        print(
            f"[ERROR] Cannot get PR info (local): status={resp.status_code}, body={resp.text[:200]}"
        )
        sys.exit(1)

    pr_data = resp.json()
    pr_number = pr_data.get("number", local_pr)
    _print_and_validate_pr(pr_data, pr_number)


def _print_and_validate_pr(pr_data: dict, pr_number):
    additions = pr_data.get("additions", 0)
    deletions = pr_data.get("deletions", 0)
    changed_lines = additions + deletions

    pr_type, limit = detect_pr_type(pr_data)

    print(
        f"\nPR size check:\n"
        f"PR #{pr_number}: {pr_data.get('title')}\n"
        f"Additions: {additions}, deletions: {deletions}, "
        f"total changed lines: {changed_lines}\n"
        f"Type: {pr_type}, limit: {limit}"
    )

    if changed_lines > limit:
        print(
            f"[FAIL] PR too large for type '{pr_type}'. "
            f"Limit: {limit}, actual: {changed_lines}"
        )
        sys.exit(1)

    print("[OK] PR size within allowed limit.")


def main():
    # Список участинков
    list_repo_members()

    # PR checks
    check_pr_size_ci() # CI
    #check_pr_size_local( ) # Локально


if __name__ == "__main__":
    main()
