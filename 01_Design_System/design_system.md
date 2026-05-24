# ISYouth Worship App - Design System

## Brand Identity
**Team:** ISYouth Worship
**Logo Colors:** Black → Indigo → Coral/Red gradient

---

## 🎨 Color Palette

| Token | Hex | RGB | Usage |
|-------|-----|-----|-------|
| `--isy-black` | `#0A0A0F` | rgb(10,10,15) | Primary background, dark surfaces |
| `--isy-surface` | `#1A1A24` | rgb(26,26,36) | Cards, inputs, modals |
| `--isy-surface-elevated` | `#252532` | rgb(37,37,50) | Elevated cards, hover states |
| `--isy-indigo` | `#4A3B6B` | rgb(74,59,107) | Secondary accent, gradient mid |
| `--isy-coral` | `#E85D5D` | rgb(232,93,93) | Primary CTA, active states, alerts |
| `--isy-coral-light` | `#FF7A7A` | rgb(255,122,122) | Hover on coral, highlights |
| `--isy-white` | `#FFFFFF` | rgb(255,255,255) | Primary text, lyrics |
| `--isy-gray-100` | `#E5E5E5` | rgb(229,229,229) | Secondary headings |
| `--isy-gray-300` | `#9CA3AF` | rgb(156,163,175) | Metadata, captions |
| `--isy-gray-500` | `#6B7280` | rgb(107,114,128) | Disabled, placeholders |
| `--isy-success` | `#10B981` | rgb(16,185,129) | Confirmations, checkmarks |
| `--isy-warning` | `#F59E0B` | rgb(245,158,11) | Warnings, timers |

### Gradients
```css
/* Primary Brand Gradient */
--gradient-brand: linear-gradient(135deg, #0A0A0F 0%, #4A3B6B 50%, #E85D5D 100%);

/* Header Gradient */
--gradient-header: linear-gradient(90deg, #0A0A0F 0%, #1A1A24 100%);

/* Coral Glow */
--gradient-coral: linear-gradient(135deg, #E85D5D 0%, #FF7A7A 100%);

/* Dark Elevated */
--gradient-dark: linear-gradient(180deg, #1A1A24 0%, #0A0A0F 100%);
```

---

## 🔤 Typography

| Style | Font | Size | Weight | Line Height | Letter Spacing | Usage |
|-------|------|------|--------|-------------|----------------|-------|
| **Display** | Inter/Poppins | 32px | 700 (Bold) | 1.2 | -0.5px | Splash, main titles |
| **H1** | Inter/Poppins | 24px | 700 | 1.3 | -0.3px | Screen titles |
| **H2** | Inter/Poppins | 20px | 600 | 1.4 | -0.2px | Section headers |
| **H3** | Inter/Poppins | 18px | 600 | 1.4 | 0px | Card titles |
| **Body** | Inter | 16px | 400 | 1.5 | 0px | Descriptions, lyrics |
| **Body Large** | Inter | 18px | 400 | 1.6 | 0px | Lyrics display (live) |
| **Caption** | Inter | 14px | 500 | 1.4 | 0.2px | Labels, metadata |
| **Small** | Inter | 12px | 400 | 1.4 | 0.3px | Timestamps, hints |
| **Button** | Inter | 16px | 600 | 1.0 | 0.5px | All buttons |
| **Nav** | Inter | 12px | 500 | 1.0 | 0.5px | Bottom nav labels |

---

## 📐 Spacing System (8px base)

| Token | Value | Usage |
|-------|-------|-------|
| `space-1` | 4px | Tight gaps |
| `space-2` | 8px | Icon gaps, tight padding |
| `space-3` | 12px | Small padding |
| `space-4` | 16px | Default padding |
| `space-5` | 20px | Card padding |
| `space-6` | 24px | Section gaps |
| `space-8` | 32px | Large gaps |
| `space-10` | 40px | Screen padding |
| `space-12` | 48px | Major sections |

---

## 🧩 Component Specs

### Buttons

**Primary Button (Coral)**
```
Background: #E85D5D
Text: #FFFFFF, 16px, weight 600
Padding: 16px 24px
Border Radius: 12px
Shadow: 0 4px 12px rgba(232, 93, 93, 0.3)
Pressed: scale 0.98, darken 10%
Disabled: #6B7280, no shadow
```

**Secondary Button (Outline)**
```
Background: transparent
Border: 1.5px solid #E85D5D
Text: #E85D5D, 16px, weight 600
Padding: 16px 24px
Border Radius: 12px
Pressed: background rgba(232, 93, 93, 0.1)
```

**Ghost Button**
```
Background: transparent
Text: #9CA3AF, 14px, weight 500
Padding: 8px 12px
Pressed: background #1A1A24
```

### Cards

**Program Card**
```
Background: #1A1A24
Border Radius: 16px
Padding: 20px
Border Left: 4px solid #E85D5D
Shadow: 0 2px 8px rgba(0, 0, 0, 0.3)
```

**Song Card**
```
Background: #1A1A24
Border Radius: 12px
Padding: 16px
Active State: border 1.5px solid #E85D5D
```

### Inputs

**Text Field**
```
Background: #1A1A24
Border: 1px solid #252532
Border Radius: 12px
Padding: 16px
Text: #FFFFFF, 16px
Placeholder: #6B7280
Focus: border #E85D5D, shadow 0 0 0 3px rgba(232,93,93,0.2)
```

### Bottom Navigation
```
Background: #0A0A0F with blur backdrop
Height: 64px + safe area
Active Icon: #E85D5D
Inactive Icon: #6B7280
Active Label: #E85D5D, 12px
Inactive Label: #6B7280, 12px
```

---

## 🌗 Theme Modes

### Dark Mode (Default)
```
Background: #0A0A0F
Surface: #1A1A24
Text Primary: #FFFFFF
Text Secondary: #9CA3AF
```

### Light Mode (Optional)
```
Background: #F5F5F7
Surface: #FFFFFF
Text Primary: #0A0A0F
Text Secondary: #6B7280
Accent: #E85D5D (same)
```

---

## 🎬 Animations

| Animation | Duration | Easing | Usage |
|-----------|----------|--------|-------|
| Fade In | 200ms | ease-out | Screen transitions |
| Slide Up | 300ms | cubic-bezier(0.4, 0, 0.2, 1) | Modals, sheets |
| Scale Press | 100ms | ease-in-out | Button presses |
| Gradient Shift | 3s | linear | Splash screen (infinite) |
| List Stagger | 50ms each | ease-out | List item appearance |
| Progress Fill | linear | song duration | Progress bars |

---

## 📱 Responsive Breakpoints

| Breakpoint | Width | Notes |
|------------|-------|-------|
| Mobile S | 320px | Minimum supported |
| Mobile M | 375px | iPhone SE/mini |
| Mobile L | 414px | iPhone Pro Max |
| Tablet | 768px | iPad mini, landscape phones |
| Tablet Pro | 1024px | iPad Pro |
