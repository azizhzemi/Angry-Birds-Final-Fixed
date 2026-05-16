---
name: Clinical Ethereal
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#434656'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#737688'
  outline-variant: '#c3c5d9'
  surface-tint: '#004ced'
  primary: '#003ec7'
  on-primary: '#ffffff'
  primary-container: '#0052ff'
  on-primary-container: '#dfe3ff'
  inverse-primary: '#b7c4ff'
  secondary: '#006d32'
  on-secondary: '#ffffff'
  secondary-container: '#55fd8c'
  on-secondary-container: '#007235'
  tertiary: '#3737c5'
  on-tertiary: '#ffffff'
  tertiary-container: '#5153de'
  on-tertiary-container: '#e3e2ff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dde1ff'
  primary-fixed-dim: '#b7c4ff'
  on-primary-fixed: '#001452'
  on-primary-fixed-variant: '#0038b6'
  secondary-fixed: '#64ff92'
  secondary-fixed-dim: '#30e375'
  on-secondary-fixed: '#00210b'
  on-secondary-fixed-variant: '#005224'
  tertiary-fixed: '#e1e0ff'
  tertiary-fixed-dim: '#c0c1ff'
  on-tertiary-fixed: '#07006c'
  on-tertiary-fixed-variant: '#2f2ebe'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: '1.1'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.6'
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.5'
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 13px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  container-max: 1280px
  gutter: 24px
  margin-desktop: 48px
  margin-mobile: 20px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 32px
---

## Brand & Style

This design system is built to bridge the gap between clinical precision and human-centric warmth. The brand personality is **authoritative yet approachable**, evoking a sense of calm reliability through "Glassmorphism" and a minimalist aesthetic. 

The target audience includes both healthcare providers who require data density and patients who need clarity and reassurance. By utilizing a "High-Tech Zen" approach, the UI minimizes cognitive load through generous whitespace and a futuristic, translucent layer logic. The emotional response should be one of **total confidence and technical sophistication**, mirroring the precision of modern medical science while maintaining the softness of high-end wellness hospitality.

## Colors

The palette is anchored by **Medical Blue (#0052FF)**, representing trust and technological depth. **Healing Green (#00D166)** is used sparingly for success states, vital signs, and positive progress indicators, ensuring they pop against the **Soft Slate (#F8FAFC)** canvas.

To achieve the futuristic glass effect, we employ a layering system of whites and semi-transparent blues. Backgrounds should never be pure white; they use the Soft Slate base to allow glass components (with high backdrop blur) to maintain visibility and depth. Accent colors for information density (like warnings or secondary data) should utilize desaturated versions of the primary blue to keep the interface feeling serene.

## Typography

This design system utilizes **Plus Jakarta Sans** for its friendly yet geometric structure. The typography scales emphasize legibility and hierarchy to ensure critical medical data is never misread.

Headlines should use tighter letter-spacing and heavier weights to feel "anchored" and professional. Body text maintains a generous line height (1.5x - 1.6x) to facilitate reading of complex medical reports. Mobile typography shifts to a slightly more condensed scale, where `display-lg` is reduced to 32px to ensure titles do not wrap awkwardly on small devices.

## Layout & Spacing

The layout follows a **Fixed-Fluid Hybrid Grid**. On desktop, content is contained within a 1280px max-width, 12-column grid to maintain the "Notion-like" clean structure. On smaller screens, the system transitions to a fluid 4-column grid with increased side margins to keep content centered and focused.

A "Generous Whitespace" rule is applied: vertical rhythm should favor larger gaps (32px+) between major sections to prevent the UI from feeling cluttered or "hospital-heavy." Components use an 8px base unit for internal padding and external margins.

## Elevation & Depth

Depth in this design system is achieved through **multi-layered translucency** rather than heavy shadows. 

1.  **Base Layer:** The Soft Slate (#F8FAFC) background.
2.  **Surface Layer:** Glassmorphic containers with `backdrop-filter: blur(20px)`, a 70% white opacity, and a very thin (1px) border of `rgba(0, 82, 255, 0.1)`.
3.  **Floating Layer:** Used for modals and dropdowns. These use a slightly more opaque background with a **large, diffused "Ambient Shadow"** (0px 20px 50px rgba(0, 82, 255, 0.05)).

This stacking creates a sense of "physical glass" sheets floating in a clean, digital space, evoking a futuristic lab or high-end clinic environment.

## Shapes

The shape language is defined by **large radii** to convey safety and approachability. Sharp corners are avoided entirely as they feel too industrial or aggressive for a healing environment. 

Standard components (buttons, inputs) use a **16px (1rem)** radius. Larger containers like patient cards or dashboard modules use **24px (1.5rem)**. This extreme rounding, combined with glass effects, results in a "smooth-to-the-touch" digital feel inspired by modern high-end software like Stripe.

## Components

### Buttons
Primary buttons use a solid Medical Blue (#0052FF) with a subtle inner glow. Secondary buttons use a glass effect with a subtle blue border. All buttons have a hover state that slightly increases the backdrop blur or adds a soft blue outer glow.

### Cards
Cards are the primary organizational unit. They must feature a 1px stroke in a light blue tint to define their edges against the Soft Slate background. Content inside cards should be padded at 24px minimum.

### Inputs & Selects
Input fields are "ghost-styled" with a light grey background that turns white on focus. They use the same 16px radius as buttons. Labels are positioned above the field in `label-sm` typography to maintain a structured, form-heavy logic similar to Notion.

### Vital Status Chips
Small, rounded-pill indicators for patient status. They should use a "subtle-tint" background (e.g., Healing Green at 10% opacity) with high-contrast text for maximum readability without being visually loud.

### Glass Modals
Modals should take up significant screen real estate, using a heavy backdrop-blur (40px) on the content behind them to create a "focus mode" for the user.