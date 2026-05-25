/* ──────────────────────────────────────────────────────────────
   app.js – Main initialization
   NeuroBrand Landing Page
   ────────────────────────────────────────────────────────────── */

document.addEventListener('DOMContentLoaded', function () {
  var prefersReducedMotion = window.matchMedia(
    '(prefers-reduced-motion: reduce)'
  ).matches;

  /* ── 1. Particles ────────────────────────────────────────── */

  if (typeof ParticleSystem !== 'undefined' && !prefersReducedMotion) {
    try {
      new ParticleSystem('hero-particles');
    } catch (e) {
      console.warn('[NeuroBrand] Particle system failed to init:', e);
    }
  }

  /* ── 2. Scroll Animations ────────────────────────────────── */

  if (typeof initScrollAnimations === 'function' && !prefersReducedMotion) {
    try {
      initScrollAnimations();
    } catch (e) {
      console.warn('[NeuroBrand] Scroll animations failed to init:', e);
    }
  }

  // If user prefers reduced motion, make everything visible immediately
  if (prefersReducedMotion) {
    _showAllContent();
  }

  /* ── 3. Navigation ──────────────────────────────────────── */

  if (typeof initNavigation === 'function') {
    try {
      initNavigation();
    } catch (e) {
      console.warn('[NeuroBrand] Navigation failed to init:', e);
    }
  }

  /* ── 4. Forms ───────────────────────────────────────────── */

  if (typeof initForms === 'function') {
    try {
      initForms();
    } catch (e) {
      console.warn('[NeuroBrand] Forms failed to init:', e);
    }
  }

  /* ── 5. Tech Wave SVG ───────────────────────────────────── */

  if (!prefersReducedMotion) {
    initTechWave();
  }

  /* ── 6. Page Load Transition ─────────────────────────────── */

  // Small delay so the browser can paint the initial frame
  requestAnimationFrame(function () {
    requestAnimationFrame(function () {
      document.body.classList.remove('loading');
      document.body.classList.add('loaded');
    });
  });
});

/* ──────────────────────────────────────────────────────────────
   initTechWave – Animates SVG heart-rate / brainwave path
   Uses stroke-dasharray + dashoffset for a "drawing" effect,
   then a continuous flowing pulse loop.
   ────────────────────────────────────────────────────────────── */

function initTechWave() {
  var wavePaths = document.querySelectorAll(
    '.tech-wave path, .technology__wave path, .wave-line path, #tech-wave path'
  );

  if (!wavePaths.length) return;

  wavePaths.forEach(function (path) {
    var length = path.getTotalLength ? path.getTotalLength() : 1000;

    // Set initial state: hidden via dashoffset
    path.style.strokeDasharray = length;
    path.style.strokeDashoffset = length;

    // Check if GSAP is available for a nicer animation
    if (typeof gsap !== 'undefined' && typeof ScrollTrigger !== 'undefined') {
      // Draw-in effect triggered on scroll
      gsap.to(path, {
        strokeDashoffset: 0,
        duration: 2,
        ease: 'power2.inOut',
        scrollTrigger: {
          trigger: path.closest('section') || path.closest('.technology') || path,
          start: 'top 75%',
          toggleActions: 'play none none none',
          once: true,
        },
        onComplete: function () {
          // After draw-in, start the flowing pulse
          _startWavePulse(path, length);
        },
      });
    } else {
      // CSS-only fallback: simple animation
      path.style.transition = 'stroke-dashoffset 2s ease-in-out';
      // Use IntersectionObserver for trigger
      var observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
          if (entry.isIntersecting) {
            path.style.strokeDashoffset = '0';
            observer.unobserve(path);
            setTimeout(function () {
              _startWavePulseCSS(path, length);
            }, 2100);
          }
        });
      }, { threshold: 0.3 });
      observer.observe(path.closest('section') || path);
    }
  });
}

/**
 * Continuous flowing pulse using GSAP
 */
function _startWavePulse(path, length) {
  // Subtle cycling dashoffset for a flowing feel
  gsap.to(path, {
    strokeDashoffset: -length,
    duration: 8,
    ease: 'none',
    repeat: -1,
  });

  // Reset dasharray to create a segmented look
  path.style.strokeDasharray = length * 0.7 + ' ' + length * 0.3;
}

/**
 * CSS-only fallback for wave pulse
 */
function _startWavePulseCSS(path, length) {
  path.style.strokeDasharray = length * 0.7 + ' ' + length * 0.3;
  path.style.transition = 'none';

  // Inject animation
  var animName = 'wavePulse_' + Math.random().toString(36).slice(2, 8);
  var style = document.createElement('style');
  style.textContent =
    '@keyframes ' + animName + ' {' +
    '  from { stroke-dashoffset: 0; }' +
    '  to { stroke-dashoffset: -' + length + 'px; }' +
    '}';
  document.head.appendChild(style);
  path.style.animation = animName + ' 8s linear infinite';
}

/* ──────────────────────────────────────────────────────────────
   _showAllContent – For prefers-reduced-motion
   Makes all animated elements visible immediately.
   ────────────────────────────────────────────────────────────── */

function _showAllContent() {
  // Add is-visible to common animated containers
  var selectors = [
    '.hero',
    '.hero__title', '.hero__subtitle', '.hero__actions',
    '.hero__badge', '.hero__eyebrow', '.hero__proof',
    '.storytelling__item',
    '.benefit-card',
    '.technology__dashboard',
    '.technology__panel', '.dashboard__panel', '.tech-panel',
    '.privacy', '.privacy__card', '.privacy-card', '.privacy__item',
    '.cta', '.cta__content',
    '.footer', 'footer',
  ];

  selectors.forEach(function (sel) {
    var els = document.querySelectorAll(sel);
    els.forEach(function (el) {
      el.classList.add('is-visible');
      el.style.opacity = '1';
      el.style.transform = 'none';
    });
  });

  // Set metric bar widths instantly
  var bars = document.querySelectorAll(
    '.metric-bar__fill, .progress-bar__fill, [data-width]'
  );
  bars.forEach(function (bar) {
    var w = bar.getAttribute('data-width') || bar.style.width;
    if (w) bar.style.width = w;
  });

  // Show tech wave paths
  var paths = document.querySelectorAll(
    '.tech-wave path, .technology__wave path, .wave-line path, #tech-wave path'
  );
  paths.forEach(function (path) {
    path.style.strokeDashoffset = '0';
    path.style.strokeDasharray = 'none';
  });

  // Inject a style that ensures nothing is hidden
  var style = document.createElement('style');
  style.id = 'neurobrand-reduced-motion';
  style.textContent =
    '*, *::before, *::after {' +
    '  animation-duration: 0.01ms !important;' +
    '  animation-iteration-count: 1 !important;' +
    '  transition-duration: 0.01ms !important;' +
    '  scroll-behavior: auto !important;' +
    '}';
  document.head.appendChild(style);
}
