<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    
    <!-- SEO Meta Tags -->
    <title><?php echo htmlspecialchars($pageTitle ?? 'NeuroBrand'); ?></title>
    <meta name="description" content="<?php echo htmlspecialchars($pageDescription ?? ''); ?>">
    <meta name="keywords" content="<?php echo htmlspecialchars($pageKeywords ?? ''); ?>">
    <meta name="author" content="NeuroBrand Bio-Tech">
    <meta name="robots" content="index, follow">
    
    <!-- Open Graph -->
    <meta property="og:type" content="website">
    <meta property="og:title" content="<?php echo htmlspecialchars($pageTitle ?? 'NeuroBrand'); ?>">
    <meta property="og:description" content="<?php echo htmlspecialchars($pageDescription ?? ''); ?>">
    <meta property="og:image" content="/public/images/wristband-hero.png">
    <meta property="og:url" content="https://neurobrand.com">
    <meta property="og:site_name" content="NeuroBrand">
    
    <!-- Twitter Card -->
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="<?php echo htmlspecialchars($pageTitle ?? 'NeuroBrand'); ?>">
    <meta name="twitter:description" content="<?php echo htmlspecialchars($pageDescription ?? ''); ?>">
    
    <!-- Favicon -->
    <link rel="icon" type="image/svg+xml" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32'><defs><linearGradient id='g' x1='0%25' y1='0%25' x2='100%25' y2='100%25'><stop offset='0%25' stop-color='%23AFCBFF'/><stop offset='50%25' stop-color='%23B7F5D1'/><stop offset='100%25' stop-color='%23DCCBFF'/></linearGradient></defs><circle cx='16' cy='16' r='14' fill='url(%23g)'/></svg>">
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    
    <!-- Stylesheets -->
    <link rel="stylesheet" href="/public/css/variables.css">
    <link rel="stylesheet" href="/public/css/base.css">
    <link rel="stylesheet" href="/public/css/animations.css">
    <link rel="stylesheet" href="/public/css/layout.css">
    <link rel="stylesheet" href="/public/css/components.css">
    <link rel="stylesheet" href="/public/css/hero.css">
    <link rel="stylesheet" href="/public/css/storytelling.css">
    <link rel="stylesheet" href="/public/css/benefits.css">
    <link rel="stylesheet" href="/public/css/technology.css">
    <link rel="stylesheet" href="/public/css/privacy.css">
    <link rel="stylesheet" href="/public/css/cta.css">
</head>
<body class="loading">

    <!-- Navigation -->
    <nav class="nav" id="main-nav" role="navigation" aria-label="Navegación principal">
        <div class="nav__inner container">
            <a href="/" class="nav__logo" aria-label="NeuroBrand - Inicio">
                <span class="nav__logo-icon" aria-hidden="true"></span>
                <span class="nav__logo-text">NeuroBrand</span>
            </a>
            
            <ul class="nav__links" id="nav-links" role="menubar">
                <li role="none"><a href="#storytelling" class="nav__link" role="menuitem">Producto</a></li>
                <li role="none"><a href="#technology" class="nav__link" role="menuitem">Tecnología</a></li>
                <li role="none"><a href="#privacy" class="nav__link" role="menuitem">Privacidad</a></li>
            </ul>
            
            <a href="#waitlist" class="btn btn--primary nav__cta">Lista de espera</a>
            
            <button class="nav__toggle" id="nav-toggle" aria-label="Menú de navegación" aria-expanded="false">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round">
                    <line x1="3" y1="6" x2="21" y2="6" class="nav__toggle-line nav__toggle-line--1"/>
                    <line x1="3" y1="12" x2="21" y2="12" class="nav__toggle-line nav__toggle-line--2"/>
                    <line x1="3" y1="18" x2="21" y2="18" class="nav__toggle-line nav__toggle-line--3"/>
                </svg>
            </button>
        </div>
    </nav>

    <!-- Main Content -->
    <main id="main-content">
