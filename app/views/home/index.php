<?php
/**
 * Home Page View
 * 
 * Assembles the complete landing page from layout and partials.
 */

// Layout: Header (includes <head>, nav, opens <main>)
require APP_PATH . '/views/layout/header.php';

// Section 1: Hero
require APP_PATH . '/views/partials/hero.php';

// Section 2: Scroll Storytelling
require APP_PATH . '/views/partials/storytelling.php';

// Section 3: Benefits
require APP_PATH . '/views/partials/benefits.php';

// Section 4: Technology + AI
require APP_PATH . '/views/partials/technology.php';

// Section 5: Privacy & Ethics
require APP_PATH . '/views/partials/privacy.php';

// Section 7: Final CTA
require APP_PATH . '/views/partials/cta.php';

// Layout: Footer (scripts, closes </body></html>)
require APP_PATH . '/views/layout/footer.php';
