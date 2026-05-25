/* ──────────────────────────────────────────────────────────────
   nav.js – Navigation behavior
   NeuroBrand Landing Page
   ────────────────────────────────────────────────────────────── */

function initNavigation() {
  var nav = document.querySelector('.nav');
  var toggle = document.querySelector('.nav__toggle');
  var links = document.querySelector('.nav__links');
  var navHeight = 72;

  if (!nav) return;

  /* ── Scroll → .nav--scrolled ─────────────────────────────── */

  var scrollThreshold = 50;
  var ticking = false;

  function onScroll() {
    if (!ticking) {
      requestAnimationFrame(function () {
        if (window.scrollY > scrollThreshold) {
          nav.classList.add('nav--scrolled');
        } else {
          nav.classList.remove('nav--scrolled');
        }
        ticking = false;
      });
      ticking = true;
    }
  }

  window.addEventListener('scroll', onScroll, { passive: true });
  // Set initial state
  onScroll();

  /* ── Mobile Toggle ───────────────────────────────────────── */

  if (toggle && links) {
    toggle.addEventListener('click', function () {
      var isOpen = links.classList.toggle('nav__links--open');
      toggle.classList.toggle('nav__toggle--active', isOpen);
      toggle.setAttribute('aria-expanded', String(isOpen));

      // Prevent body scroll when menu is open
      document.body.style.overflow = isOpen ? 'hidden' : '';
    });
  }

  /* ── Smooth Scroll for Anchor Links ──────────────────────── */

  var navAnchors = nav.querySelectorAll('a[href^="#"]');

  navAnchors.forEach(function (anchor) {
    anchor.addEventListener('click', function (e) {
      var targetId = this.getAttribute('href');
      if (!targetId || targetId === '#') return;

      var targetEl = document.querySelector(targetId);
      if (!targetEl) return;

      e.preventDefault();

      var targetPosition =
        targetEl.getBoundingClientRect().top + window.scrollY - navHeight;

      window.scrollTo({
        top: targetPosition,
        behavior: 'smooth',
      });

      // Close mobile menu
      if (links) {
        links.classList.remove('nav__links--open');
        if (toggle) {
          toggle.classList.remove('nav__toggle--active');
          toggle.setAttribute('aria-expanded', 'false');
        }
        document.body.style.overflow = '';
      }
    });
  });

  /* ── Active Link Tracking (IntersectionObserver) ─────────── */

  var sections = document.querySelectorAll('section[id]');
  if (!sections.length) return;

  var observerOptions = {
    root: null,
    rootMargin: '-' + navHeight + 'px 0px -40% 0px',
    threshold: 0,
  };

  var currentActive = null;

  var observer = new IntersectionObserver(function (entries) {
    entries.forEach(function (entry) {
      if (entry.isIntersecting) {
        var id = entry.target.getAttribute('id');
        _setActiveLink(id);
      }
    });
  }, observerOptions);

  sections.forEach(function (section) {
    observer.observe(section);
  });

  function _setActiveLink(id) {
    if (currentActive === id) return;
    currentActive = id;

    navAnchors.forEach(function (anchor) {
      var href = anchor.getAttribute('href');
      if (href === '#' + id) {
        anchor.classList.add('nav__link--active');
        anchor.setAttribute('aria-current', 'true');
      } else {
        anchor.classList.remove('nav__link--active');
        anchor.removeAttribute('aria-current');
      }
    });
  }

  /* ── Close menu on outside click ─────────────────────────── */

  document.addEventListener('click', function (e) {
    if (
      links &&
      links.classList.contains('nav__links--open') &&
      !nav.contains(e.target)
    ) {
      links.classList.remove('nav__links--open');
      if (toggle) {
        toggle.classList.remove('nav__toggle--active');
        toggle.setAttribute('aria-expanded', 'false');
      }
      document.body.style.overflow = '';
    }
  }, { passive: true });

  /* ── Keyboard: Escape closes menu ────────────────────────── */

  document.addEventListener('keydown', function (e) {
    if (
      e.key === 'Escape' &&
      links &&
      links.classList.contains('nav__links--open')
    ) {
      links.classList.remove('nav__links--open');
      if (toggle) {
        toggle.classList.remove('nav__toggle--active');
        toggle.setAttribute('aria-expanded', 'false');
        toggle.focus();
      }
      document.body.style.overflow = '';
    }
  });
}
