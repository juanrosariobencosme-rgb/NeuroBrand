/* ──────────────────────────────────────────────────────────────
   form.js – Waitlist form handling
   NeuroBrand Landing Page
   ────────────────────────────────────────────────────────────── */

function initForms() {
  var forms = document.querySelectorAll('.waitlist-form');
  if (!forms.length) return;

  forms.forEach(function (form) {
    _setupForm(form);
  });
}

/* ── Per-form Setup ───────────────────────────────────────── */

function _setupForm(form) {
  var input = form.querySelector('input[type="email"], input[name="email"]');
  var button = form.querySelector('button[type="submit"], button');
  var isSubmitting = false;
  var originalButtonText = button ? button.textContent : 'Join Waitlist';

  // Create message element if it doesn't exist
  var messageEl = form.querySelector('.form-message');
  if (!messageEl) {
    messageEl = document.createElement('div');
    messageEl.className = 'form-message';
    messageEl.setAttribute('role', 'alert');
    messageEl.setAttribute('aria-live', 'polite');
    form.appendChild(messageEl);
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();

    if (isSubmitting) return;
    if (!input) return;

    var email = input.value.trim();

    // Clear previous messages
    _clearMessage(messageEl);
    input.classList.remove('input--error', 'input--success');

    // Validate
    if (!email) {
      _showError(input, messageEl, 'Please enter your email address.');
      return;
    }

    if (!_isValidEmail(email)) {
      _showError(input, messageEl, 'Please enter a valid email address.');
      return;
    }

    // Submit
    isSubmitting = true;
    _setLoading(button, true);

    fetch('/subscribe', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      body: JSON.stringify({ email: email }),
    })
      .then(function (response) {
        if (!response.ok) {
          return response.json().then(function (data) {
            throw new Error(data.message || 'Something went wrong. Please try again.');
          }).catch(function (err) {
            if (err.message && err.message !== 'Something went wrong. Please try again.') {
              throw err;
            }
            throw new Error('Something went wrong. Please try again.');
          });
        }
        return response.json();
      })
      .then(function (data) {
        _showSuccess(
          input,
          messageEl,
          data.message || "You're on the list! We'll be in touch soon."
        );
        input.value = '';
      })
      .catch(function (err) {
        _showError(
          input,
          messageEl,
          err.message || 'Something went wrong. Please try again.'
        );
      })
      .finally(function () {
        isSubmitting = false;
        _setLoading(button, false, originalButtonText);
      });
  });

  // Clear error state on input
  if (input) {
    input.addEventListener('input', function () {
      input.classList.remove('input--error');
      if (messageEl.classList.contains('form-message--error')) {
        _clearMessage(messageEl);
      }
    }, { passive: true });
  }
}

/* ── Validation ───────────────────────────────────────────── */

function _isValidEmail(email) {
  // Covers 99% of practical cases – intentionally not RFC 5322 strict
  return /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email);
}

/* ── UI Helpers ───────────────────────────────────────────── */

function _setLoading(button, loading, originalText) {
  if (!button) return;

  if (loading) {
    button.disabled = true;
    button.setAttribute('data-original-text', button.textContent);
    button.textContent = '...';
    button.classList.add('btn--loading');
  } else {
    button.disabled = false;
    button.textContent = originalText || button.getAttribute('data-original-text') || 'Join Waitlist';
    button.classList.remove('btn--loading');
  }
}

function _showSuccess(input, messageEl, text) {
  if (input) {
    input.classList.add('input--success');
    input.classList.remove('input--error');
  }

  messageEl.textContent = text;
  messageEl.className = 'form-message form-message--success';
  messageEl.style.display = 'block';

  // Animate in
  messageEl.style.opacity = '0';
  messageEl.style.transform = 'translateY(8px)';
  requestAnimationFrame(function () {
    messageEl.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
    messageEl.style.opacity = '1';
    messageEl.style.transform = 'translateY(0)';
  });

  // Auto-hide success after 6 seconds
  setTimeout(function () {
    messageEl.style.opacity = '0';
    messageEl.style.transform = 'translateY(-4px)';
    setTimeout(function () {
      _clearMessage(messageEl);
      if (input) input.classList.remove('input--success');
    }, 400);
  }, 6000);
}

function _showError(input, messageEl, text) {
  if (input) {
    input.classList.add('input--error');
    input.classList.remove('input--success');

    // Shake animation
    input.style.animation = 'none';
    // Force reflow
    void input.offsetWidth;
    input.style.animation = 'formShake 0.5s ease';
  }

  messageEl.textContent = text;
  messageEl.className = 'form-message form-message--error';
  messageEl.style.display = 'block';

  // Animate in
  messageEl.style.opacity = '0';
  messageEl.style.transform = 'translateY(8px)';
  requestAnimationFrame(function () {
    messageEl.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
    messageEl.style.opacity = '1';
    messageEl.style.transform = 'translateY(0)';
  });
}

function _clearMessage(messageEl) {
  messageEl.textContent = '';
  messageEl.className = 'form-message';
  messageEl.style.display = 'none';
  messageEl.style.opacity = '';
  messageEl.style.transform = '';
  messageEl.style.transition = '';
}

/* ── Inject shake keyframes if not already present ────────── */

(function injectShakeKeyframes() {
  if (document.getElementById('neurobrand-form-keyframes')) return;

  var style = document.createElement('style');
  style.id = 'neurobrand-form-keyframes';
  style.textContent =
    '@keyframes formShake {' +
    '  0%, 100% { transform: translateX(0); }' +
    '  10%, 50%, 90% { transform: translateX(-4px); }' +
    '  30%, 70% { transform: translateX(4px); }' +
    '}';
  document.head.appendChild(style);
})();
