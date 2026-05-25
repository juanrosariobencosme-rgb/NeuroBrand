<?php
/**
 * NeuroBrand — Entry Point / Router
 * 
 * Simple MVC router for the landing page.
 * All requests are routed through this file via .htaccess
 */

// ── Configuration ──────────────────────────────────────────
define('BASE_PATH', __DIR__);
define('APP_PATH', BASE_PATH . '/app');
define('PUBLIC_PATH', BASE_PATH . '/public');
define('DATA_PATH', BASE_PATH . '/data');

// ── Autoload Controllers & Models ──────────────────────────
require_once APP_PATH . '/controllers/HomeController.php';
require_once APP_PATH . '/models/Subscriber.php';

// ── Simple Router ──────────────────────────────────────────
$requestUri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$requestMethod = $_SERVER['REQUEST_METHOD'];

// Remove trailing slash (except root)
if ($requestUri !== '/' && substr($requestUri, -1) === '/') {
    $requestUri = rtrim($requestUri, '/');
}

// ── Route Definitions ──────────────────────────────────────
$controller = new HomeController();

switch (true) {
    // Home page
    case ($requestUri === '/' && $requestMethod === 'GET'):
        $controller->index();
        break;

    // Subscribe endpoint (AJAX POST)
    case ($requestUri === '/subscribe' && $requestMethod === 'POST'):
        $controller->subscribe();
        break;

    // Serve static files (fallback for PHP built-in server)
    case (preg_match('/^\/public\//', $requestUri)):
        $filePath = BASE_PATH . $requestUri;
        if (file_exists($filePath)) {
            $ext = pathinfo($filePath, PATHINFO_EXTENSION);
            $mimeTypes = [
                'css'  => 'text/css',
                'js'   => 'application/javascript',
                'png'  => 'image/png',
                'jpg'  => 'image/jpeg',
                'jpeg' => 'image/jpeg',
                'gif'  => 'image/gif',
                'svg'  => 'image/svg+xml',
                'webp' => 'image/webp',
                'woff' => 'font/woff',
                'woff2' => 'font/woff2',
                'ico'  => 'image/x-icon',
            ];
            if (isset($mimeTypes[$ext])) {
                header('Content-Type: ' . $mimeTypes[$ext]);
            }
            readfile($filePath);
        } else {
            http_response_code(404);
            echo '404 — File not found';
        }
        break;

    // 404
    default:
        http_response_code(404);
        echo '<!DOCTYPE html><html><head><title>404</title></head><body><h1>404 — Página no encontrada</h1></body></html>';
        break;
}
