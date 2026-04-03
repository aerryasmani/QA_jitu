# QA_jitu

Quality assurance for **[jitu.one](https://jitu.one)** — the public marketing landing page (unauthenticated). This repository combines **written test specifications** in Markdown with **Java UI automation** (Selenium + JUnit 5 + Maven).

## Stack overview

| Layer | What we use |
| --- | --- |
| **Specs** | Markdown cases under `TestSuite/Landing page/TestCases/` — checklist and traceability IDs (**LP-***) |
| **Automation** | Java **17**, **Selenium 4**, **JUnit 5**, **Maven** — project in **`Automation/`** |
| **Browser** | **Google Chrome** (headless by default in `BaseTest`); Selenium Manager resolves the driver when possible |

Together this is a typical **full-stack QA setup**: human-readable specs, executable UI tests, and room to add CI later.

## What is here now

- **`TestSuite/Landing page/TestCases/`** — Markdown test cases aligned to a content snapshot of the live site (hero, how-it-works, features, roadmap, CTAs, footer, etc.).
- Each case file uses a shared table format: ID, title, priority, type, preconditions, steps, and expected results.
- **`TC-LP-00-overview-and-legend.md`** is the entry point: file index, legend (priorities and types), regression triggers, and traceability notes.
- **`Automation/`** — Maven project that runs Selenium tests; map automated tests to the same **LP-*** / **TC-LP-** IDs as the Markdown suite.

## Test case files (Markdown)

| File | Focus |
| --- | --- |
| `TC-LP-00-overview-and-legend.md` | Overview, legend, regression hints |
| `TC-LP-01-page-load-and-shell.md` | Load, title, favicon, document shell |
| `TC-LP-02-hero-and-ctas.md` | Hero and primary/secondary CTAs |
| `TC-LP-03-demo-output-preview.md` | Demo / sample output |
| `TC-LP-04-metrics-trust-strip.md` | Metrics and trust strip |
| `TC-LP-05-how-it-works.md` | How it works (steps) |
| `TC-LP-06-core-features.md` | Core features |
| `TC-LP-07-output-format-table.md` | Output format / sample table |
| `TC-LP-08-built-for-personas.md` | Personas section |
| `TC-LP-09-roadmap.md` | Roadmap |
| `TC-LP-10-footer-closing-cta.md` | Footer and closing CTAs |
| `TC-LP-11-cross-cutting-a11y-performance.md` | A11y, performance, HTTPS, 404 |

## Automation — prerequisites

- **JDK 17** (`JAVA_HOME` set)
- **Apache Maven 3.8+** on your `PATH`
- **Google Chrome** installed (matches what Selenium drives in this project)

## Automation — run tests (Maven)

Run all commands from the **repository root**, or `cd Automation` and drop the `-f Automation/pom.xml` part.

### Run all tests

```bash
mvn -f Automation/pom.xml test
```

From inside `Automation/`:

```bash
cd Automation
mvn test
```

### Run one test class

Use the **Java class name** (not the `.java` file path):

```bash
mvn -f Automation/pom.xml test -Dtest=TC_LP_01_PageLoadTest
```

### Run one test method

Use `ClassName#methodName`:

```bash
mvn -f Automation/pom.xml test -Dtest=TC_LP_01_PageLoadTest#LP_001_pageTitleIsNotBlank
```

On **Windows PowerShell**, `#` starts a comment unless the argument is quoted:

```powershell
mvn -f Automation/pom.xml test "-Dtest=TC_LP_01_PageLoadTest#LP_001_pageTitleIsNotBlank"
```

### Run several classes (pattern)

Surefire accepts patterns; for example:

```bash
mvn -f Automation/pom.xml test -Dtest=TC_LP_01_PageLoadTest,com.jitu.otherpkg.AnotherTest
```

Use `*` with care (it can pull in more classes than you expect):

```bash
mvn -f Automation/pom.xml test -Dtest=*PageLoad*
```

### Useful flags

- **Skip tests** (build only): `mvn -f Automation/pom.xml package -DskipTests`
- **Quiet tests**: add `-q` to Maven for less console noise.

## Best practices — element locators (Selenium)

Stable automation depends more on **locators** than on syntax. Prefer the following order:

1. **`data-testid` (or similar contract attributes)** — Agree with dev on stable hooks such as `data-testid="nav-logo"`. These survive most CSS refactors. Example: `By.cssSelector("[data-testid='nav-logo']")` or `By.cssSelector("[data-testid=\"nav-logo\"]")`.
2. **`id`** — Good when IDs are unique and not generated per deploy. Example: `By.id("hero-cta")`.
3. **`name`** — Common for forms. Example: `By.name("email")`.
4. **Accessible roles / labels** — Aligns with a11y and tends to stay stable for interactive elements. In Selenium 4: prefer `By.role` / relative locators where they fit your style (or XPath with `aria-label` only when necessary).
5. **Structural CSS** — Use when tied to semantics (e.g. `main h1`, `header nav a:first-of-type`). Avoid overly deep chains that break on small layout changes.
6. **XPath** — Use for text or structure when no better hook exists; keep expressions short and maintainable.

**Avoid when possible**

- **Hashed or build-scoped class names** (e.g. CSS-module strings like `._logo_236fd_14`) — they change between builds and break CI without code changes.
- **Absolute XPaths** from `/html/body/...` — brittle.
- **Coordinates or pixel-based clicks** — flaky across viewports and themes.

**Practical habit**

- Centralize selectors in **page objects** or small constants so updates happen in one place.
- When the app cannot add `data-testid` yet, document the risk and prefer **role**, **label**, or **stable text** (with i18n in mind) over random class names.

## Suggested use

1. Open `TestSuite/Landing page/TestCases/TC-LP-00-overview-and-legend.md` for manual / exploratory runs.
2. Run `mvn -f Automation/pom.xml test` after changes to automation or before releases.
3. Keep Markdown **LP-*** IDs and Java `@DisplayName` / class names aligned for traceability.

## CI

No CI workflow is committed in this repo yet; when added, the same Maven command (`mvn test` from `Automation/`) is the natural entry point.

---

*Copy and URLs in the test files are meant to match the live landing page; after releases, spot-check for copy and URL drift.*
