<?php
/**
 * HomeController
 * 
 * Handles the main landing page and waitlist subscription.
 */
class HomeController
{
    /**
     * Render the landing page
     */
    public function index(): void
    {
        // Page metadata for SEO
        $pageTitle = 'NeuroBrand — Bienestar Emocional Inteligente';
        $pageDescription = 'Una pulsera inteligente impulsada por IA diseñada para ayudarte a entender, regular y equilibrar tus emociones en tiempo real.';
        $pageKeywords = 'bienestar emocional, pulsera inteligente, inteligencia artificial, salud mental, wearable, IA emocional';

        // Render the view
        require APP_PATH . '/views/home/index.php';
    }

    /**
     * Handle waitlist subscription (AJAX POST)
     */
    public function subscribe(): void
    {
        header('Content-Type: application/json; charset=utf-8');

        // Get JSON input
        $input = json_decode(file_get_contents('php://input'), true);
        
        // Fallback to form data
        if (empty($input)) {
            $input = $_POST;
        }

        $email = isset($input['email']) ? trim($input['email']) : '';

        // Validate email
        if (empty($email)) {
            http_response_code(400);
            echo json_encode([
                'success' => false,
                'message' => 'Por favor, ingresa tu email.'
            ]);
            return;
        }

        if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            http_response_code(400);
            echo json_encode([
                'success' => false,
                'message' => 'Por favor, ingresa un email válido.'
            ]);
            return;
        }

        // Save subscriber
        $subscriber = new Subscriber();
        $result = $subscriber->add($email);

        if ($result['success']) {
            http_response_code(200);
            echo json_encode([
                'success' => true,
                'message' => '¡Bienvenido! Te hemos añadido a la lista de espera. 🎉'
            ]);
        } else {
            http_response_code(409);
            echo json_encode([
                'success' => false,
                'message' => $result['message']
            ]);
        }
    }
}
