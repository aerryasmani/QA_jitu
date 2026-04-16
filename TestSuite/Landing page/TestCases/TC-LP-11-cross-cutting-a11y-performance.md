# TC-LP-11 — Cross-cutting (navigation, accessibility, performance)

| ID | Title | Priority | Type | Preconditions | Steps | Expected result |
| --- | --- | --- | --- | --- | --- | --- |
| LP-048 | Heading hierarchy | P2 | Cross-cutting | LP-001 | 1. Run axe or inspect h1–h6 | Single logical h1; sections use nested headings without skips |
| LP-049 | Keyboard navigation | P2 | Cross-cutting | LP-001 | 1. Tab through interactive elements | All CTAs and links focusable; visible focus ring |
| LP-050 | Images alt text | P2 | Cross-cutting | LP-001 | 1. Audit img / SVG decorative vs informative | Meaningful alts or aria-hidden for decorative assets |
| LP-051 | No console errors on load | P1 | Functional | LP-001 | 1. Open devtools console on fresh load | No uncaught errors blocking render |
| LP-052 | Lighthouse performance sanity | P3 | Cross-cutting | LP-001 | 1. Run Lighthouse (mobile) | Document scores; flag LCP/CLS regressions |
| LP-053 | HTTPS only | P0 | Functional | LP-001 | 1. Confirm URL scheme | Page served over HTTPS |
| LP-054 | 404 handling | P2 | Functional | — | 1. Open `https://jitu.one/nonexistent-path-xyz` | Branded 404 or redirect; no stack trace |
