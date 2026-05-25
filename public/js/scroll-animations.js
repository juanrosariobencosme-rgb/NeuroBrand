/* ──────────────────────────────────────────────────────────────
   scroll-animations.js – GSAP ScrollTrigger animations
   NeuroBrand Landing Page
   Depends on: gsap + ScrollTrigger (CDN globals)
   ────────────────────────────────────────────────────────────── */

function initScrollAnimations() {
  if (typeof gsap === 'undefined' || typeof ScrollTrigger === 'undefined') {
    console.warn('[NeuroBrand] GSAP or ScrollTrigger not loaded. Skipping scroll animations.');
    return;
  }

  gsap.registerPlugin(ScrollTrigger);

  // Global GSAP defaults
  gsap.defaults({
    ease: 'power3.out',
    duration: 1,
  });

  _animateHero();
  _animateStorytelling();
  _animateBenefits();
  _animateTechnology();
  _animatePrivacy();
  _animateCTA();
  _animateFooter();

  // Refresh ScrollTrigger after images / fonts load
  window.addEventListener('load', function () {
    ScrollTrigger.refresh();
  });
}

/* ── Hero ──────────────────────────────────────────────────── */

function _animateHero() {
  const hero = document.querySelector('.hero');
  if (!hero) return;

  const tl = gsap.timeline({ delay: 0.3 });

  // Badge / eyebrow
  const badge = hero.querySelector('.hero__badge, .hero__eyebrow');
  if (badge) {
    tl.from(badge, {
      opacity: 0,
      y: 20,
      duration: 0.6,
    });
  }

  // Headline
  const headline = hero.querySelector('.hero__title, h1');
  if (headline) {
    tl.from(headline, {
      opacity: 0,
      y: 30,
      duration: 0.9,
    }, '-=0.3');
  }

  // Sub-headline
  const subtitle = hero.querySelector('.hero__subtitle, .hero__description');
  if (subtitle) {
    tl.from(subtitle, {
      opacity: 0,
      y: 30,
      duration: 0.8,
    }, '-=0.5');
  }

  // CTA buttons / form
  const actions = hero.querySelectorAll('.hero__actions, .hero__cta, .hero__form, .waitlist-form');
  if (actions.length) {
    tl.from(actions, {
      opacity: 0,
      y: 30,
      duration: 0.8,
      stagger: 0.2,
    }, '-=0.4');
  }

  // Social proof / trust strip
  const proof = hero.querySelector('.hero__proof, .hero__trust, .hero__social-proof');
  if (proof) {
    tl.from(proof, {
      opacity: 0,
      y: 20,
      duration: 0.7,
    }, '-=0.3');
  }
}

/* ── Storytelling ─────────────────────────────────────────── */

function _animateStorytelling() {
  const items = document.querySelectorAll('.storytelling__item');
  if (!items.length) return;

  items.forEach(function (item, index) {
    const image = item.querySelector('.storytelling__image, .storytelling__visual, img');
    const content = item.querySelector('.storytelling__content, .storytelling__text');
    const metrics = item.querySelectorAll('.storytelling__metric, .metric');

    const tl = gsap.timeline({
      scrollTrigger: {
        trigger: item,
        start: 'top 80%',
        end: 'bottom 20%',
        toggleActions: 'play none none none',
        once: true,
      },
    });

    // Image slides in from left (alternate: right on even items)
    if (image) {
      const fromX = index % 2 === 0 ? -60 : 60;
      tl.from(image, {
        opacity: 0,
        x: fromX,
        duration: 1,
        ease: 'power3.out',
      });
    }

    // Content slides in from the opposite side
    if (content) {
      const fromX = index % 2 === 0 ? 60 : -60;
      tl.from(content, {
        opacity: 0,
        x: fromX,
        duration: 1,
        ease: 'power3.out',
      }, '-=0.7');
    }

    // Metrics stagger in
    if (metrics.length) {
      tl.from(metrics, {
        opacity: 0,
        y: 25,
        duration: 0.7,
        stagger: 0.15,
        ease: 'power3.out',
      }, '-=0.5');
    }

    // Parallax on image (scrub-linked)
    if (image) {
      gsap.fromTo(image, {
        y: -30,
      }, {
        y: 30,
        ease: 'power2.inOut',
        scrollTrigger: {
          trigger: item,
          start: 'top bottom',
          end: 'bottom top',
          scrub: 1,
        },
      });
    }

    // Add visible class for CSS fallback
    ScrollTrigger.create({
      trigger: item,
      start: 'top 85%',
      once: true,
      onEnter: function () {
        item.classList.add('is-visible');
      },
    });
  });
}

/* ── Benefits ─────────────────────────────────────────────── */

function _animateBenefits() {
  const section = document.querySelector('.benefits');
  if (!section) return;

  // Section header
  const header = section.querySelector('.benefits__header, .section__header, h2');
  if (header) {
    gsap.from(header, {
      opacity: 0,
      y: 30,
      duration: 0.8,
      scrollTrigger: {
        trigger: section,
        start: 'top 85%',
        toggleActions: 'play none none none',
        once: true,
      },
    });
  }

  // Cards
  const cards = section.querySelectorAll('.benefit-card');
  if (cards.length) {
    gsap.from(cards, {
      opacity: 0,
      y: 40,
      duration: 0.8,
      stagger: 0.15,
      ease: 'power3.out',
      scrollTrigger: {
        trigger: section,
        start: 'top 80%',
        toggleActions: 'play none none none',
        once: true,
        onEnter: function () {
          cards.forEach(function (c) { c.classList.add('is-visible'); });
        },
      },
    });
  }
}

/* ── Technology ───────────────────────────────────────────── */

function _animateTechnology() {
  const section = document.querySelector('.technology');
  if (!section) return;

  // Section header
  const header = section.querySelector('.technology__header, .section__header, h2');
  if (header) {
    gsap.from(header, {
      opacity: 0,
      y: 30,
      duration: 0.8,
      scrollTrigger: {
        trigger: section,
        start: 'top 80%',
        toggleActions: 'play none none none',
        once: true,
      },
    });
  }

  // Dashboard container
  const dashboard = section.querySelector('.technology__dashboard');
  if (dashboard) {
    const tl = gsap.timeline({
      scrollTrigger: {
        trigger: section,
        start: 'top 70%',
        toggleActions: 'play none none none',
        once: true,
        onEnter: function () {
          dashboard.classList.add('is-visible');
        },
      },
    });

    // Entire dashboard fades in and scales
    tl.from(dashboard, {
      opacity: 0,
      scale: 0.95,
      duration: 1,
      ease: 'power3.out',
    });

    // Individual panels stagger in
    var panels = dashboard.querySelectorAll(
      '.technology__panel, .dashboard__panel, .tech-panel'
    );
    if (panels.length) {
      tl.from(panels, {
        opacity: 0,
        y: 25,
        duration: 0.7,
        stagger: 0.12,
        ease: 'power3.out',
      }, '-=0.5');
    }

    // Metric bars animate width
    var bars = dashboard.querySelectorAll(
      '.metric-bar__fill, .progress-bar__fill, [data-width]'
    );
    if (bars.length) {
      bars.forEach(function (bar) {
        var target =
          bar.getAttribute('data-width') ||
          bar.style.width ||
          '100%';
        gsap.fromTo(bar, {
          width: '0%',
        }, {
          width: target,
          duration: 1.2,
          ease: 'power3.out',
          scrollTrigger: {
            trigger: bar,
            start: 'top 90%',
            toggleActions: 'play none none none',
            once: true,
          },
        });
      });
    }
  }
}

/* ── Privacy ──────────────────────────────────────────────── */

function _animatePrivacy() {
  const section = document.querySelector('.privacy');
  if (!section) return;

  const tl = gsap.timeline({
    scrollTrigger: {
      trigger: section,
      start: 'top 75%',
      toggleActions: 'play none none none',
      once: true,
      onEnter: function () {
        section.classList.add('is-visible');
      },
    },
  });

  // Icon / illustration
  var icon = section.querySelector(
    '.privacy__icon, .privacy__illustration, .privacy__image, svg'
  );
  if (icon) {
    tl.from(icon, {
      opacity: 0,
      scale: 0.8,
      duration: 0.7,
      ease: 'power3.out',
    });
  }

  // Title
  var title = section.querySelector('.privacy__title, h2');
  if (title) {
    tl.from(title, {
      opacity: 0,
      y: 25,
      duration: 0.7,
      ease: 'power3.out',
    }, '-=0.3');
  }

  // Subtitle
  var subtitle = section.querySelector(
    '.privacy__subtitle, .privacy__description, p'
  );
  if (subtitle) {
    tl.from(subtitle, {
      opacity: 0,
      y: 20,
      duration: 0.6,
      ease: 'power3.out',
    }, '-=0.3');
  }

  // Cards
  var cards = section.querySelectorAll(
    '.privacy__card, .privacy-card, .privacy__item'
  );
  if (cards.length) {
    tl.from(cards, {
      opacity: 0,
      y: 35,
      duration: 0.8,
      stagger: 0.15,
      ease: 'power3.out',
    }, '-=0.2');
  }
}

/* ── CTA ──────────────────────────────────────────────────── */

function _animateCTA() {
  const section = document.querySelector('.cta');
  if (!section) return;

  const tl = gsap.timeline({
    scrollTrigger: {
      trigger: section,
      start: 'top 80%',
      toggleActions: 'play none none none',
      once: true,
      onEnter: function () {
        section.classList.add('is-visible');
      },
    },
  });

  // Content block (title, subtitle)
  var content = section.querySelector(
    '.cta__content, .cta__text, .cta__header'
  );
  if (content) {
    tl.from(content, {
      opacity: 0,
      scale: 0.97,
      y: 20,
      duration: 0.9,
      ease: 'power3.out',
    });
  } else {
    // Fallback: animate title + subtitle individually
    var title = section.querySelector('h2');
    var sub = section.querySelector('p');
    if (title) {
      tl.from(title, { opacity: 0, y: 25, duration: 0.8 });
    }
    if (sub) {
      tl.from(sub, { opacity: 0, y: 20, duration: 0.7 }, '-=0.4');
    }
  }

  // Form slides up
  var form = section.querySelector('.waitlist-form, form');
  if (form) {
    tl.from(form, {
      opacity: 0,
      y: 30,
      duration: 0.8,
      ease: 'power3.out',
    }, '-=0.4');
  }
}

/* ── Footer ───────────────────────────────────────────────── */

function _animateFooter() {
  var footer = document.querySelector('.footer, footer');
  if (!footer) return;

  gsap.from(footer, {
    opacity: 0,
    y: 20,
    duration: 0.8,
    ease: 'power3.out',
    scrollTrigger: {
      trigger: footer,
      start: 'top 95%',
      toggleActions: 'play none none none',
      once: true,
      onEnter: function () {
        footer.classList.add('is-visible');
      },
    },
  });
}
