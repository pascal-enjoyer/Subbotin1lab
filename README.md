## Лабораторная работа по ТКРПП #1
Команда (гр. РИС-22-1Б):
- Баженов Тимофей Иванович
- Батин Владислав Владимирович
- Бреднев Максим Павлович
- Лихачев Данил Алексеевич
## Отчет
Для выполнения задания за основу была взята программа по переводу азбуки Морзе
- Максим
1. Создал проект, добавил всех участников команды в коллабораторы
<img width="1280" height="812" alt="image" src="https://github.com/user-attachments/assets/8ed65918-7ea1-4c3f-a179-1a0fe117bd3b" />
2. Улучшил визуальную часть разрабатываемой программы в своей ветке feature-maksim
<img width="1893" height="350" alt="image" src="https://github.com/user-attachments/assets/d941e956-2202-45d5-bc30-3709ff9118bf" />
<img width="1608" height="866" alt="image" src="https://github.com/user-attachments/assets/134c5f0b-9eeb-4615-b12b-c4d1842b67d3" />
3. Замерджил изменения со своей ветки в ветку dev
<img width="1084" height="630" alt="image" src="https://github.com/user-attachments/assets/9d0e2ff6-6063-4b0c-8ca2-87235fe67ef0" />
4. Создал пайплайн с билдом (после этого замерджил изменения)
<img width="1890" height="883" alt="image" src="https://github.com/user-attachments/assets/be56f595-7768-4879-b9b6-b5e14608d864" />
<img width="1889" height="648" alt="image" src="https://github.com/user-attachments/assets/ef766b52-f283-4624-bcf8-c67eb74a2e47" />

- Данил
1. Добавил функционал обратного перевода в своей ветке feature_danil
<img width="1899" height="284" alt="image" src="https://github.com/user-attachments/assets/c5296e41-c32c-4574-85a9-a3e77fa2d369" />
<img width="1617" height="854" alt="image" src="https://github.com/user-attachments/assets/d100bcbc-01c3-4b82-8efd-af125b77cca6" />
2. Замерджил изменения из своей ветки в ветку dev
<img width="984" height="566" alt="image" src="https://github.com/user-attachments/assets/b0d0f2bc-f1e4-4456-b76e-934eb154688a" />
3. Добавил в пайплайн стейдж release (после этого замерджил изменения)
<img width="1890" height="347" alt="image" src="https://github.com/user-attachments/assets/b4631138-f290-4864-b182-9708a3b2d8fd" />
<img width="1884" height="884" alt="image" src="https://github.com/user-attachments/assets/ae4e7b8c-ea3a-4567-aa2b-c601b51e2652" />

- Влад
1. Добавил новую функциональность — автоматический перевод текста между обычным текстом и азбукой Морзе в режиме реального времени, без необходимости нажимать кнопку "Translate"
<img width="582" height="725" alt="image" src="https://github.com/user-attachments/assets/5c34a827-96e4-40e0-813e-6493ae1b7a91" />

2. Выполнил Pull Request, в котором изменения из рабочей ветки feature_vlad были успешно слиты в основную ветку разработки dev.
PR прошёл проверку, автоматический пайплайн успешно отработал, после чего ветка feature_vlad была безопасно объединена с dev.
<img width="699" height="624" alt="image" src="https://github.com/user-attachments/assets/1e690e62-23df-406e-a089-389fdadf5831" />

3. Добавил Python-скрипты, предназначенные для автоматизации проверки репозитория в GitHub Actions.
Скрипт для вывода участников репозитория:
-Разработан модуль, который через GitHub API автоматически получает:
    список collaborators,
    список contributors.
Скрипт проверки размера Pull Request:
-Автоматически определяет тип PR по имени ветки / тайтлу (feature/, bugfix/, refactor/).
-Проверяет количество изменённых строк (additions + deletions).
<img width="744" height="944" alt="image" src="https://github.com/user-attachments/assets/91a52d49-255e-4d9e-96e7-a066c22bfddd" />

- Тимофей



