<?php
/**
 * Subscriber Model
 * 
 * Manages waitlist subscribers stored in a JSON file.
 */
class Subscriber
{
    private string $dataFile;

    public function __construct()
    {
        $this->dataFile = DATA_PATH . '/subscribers.json';
        $this->ensureDataFile();
    }

    /**
     * Ensure the data directory and file exist
     */
    private function ensureDataFile(): void
    {
        $dir = dirname($this->dataFile);
        if (!is_dir($dir)) {
            mkdir($dir, 0755, true);
        }
        if (!file_exists($this->dataFile)) {
            file_put_contents($this->dataFile, json_encode([], JSON_PRETTY_PRINT));
        }
    }

    /**
     * Get all subscribers
     */
    public function getAll(): array
    {
        $data = file_get_contents($this->dataFile);
        return json_decode($data, true) ?? [];
    }

    /**
     * Add a new subscriber
     */
    public function add(string $email): array
    {
        $email = strtolower(trim($email));
        $subscribers = $this->getAll();

        // Check for duplicates
        foreach ($subscribers as $sub) {
            if ($sub['email'] === $email) {
                return [
                    'success' => false,
                    'message' => 'Este email ya está en nuestra lista de espera.'
                ];
            }
        }

        // Add new subscriber
        $subscribers[] = [
            'email'      => $email,
            'created_at' => date('Y-m-d H:i:s'),
            'ip'         => $_SERVER['REMOTE_ADDR'] ?? 'unknown',
            'user_agent' => $_SERVER['HTTP_USER_AGENT'] ?? 'unknown'
        ];

        // Save
        file_put_contents(
            $this->dataFile,
            json_encode($subscribers, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE)
        );

        return [
            'success' => true,
            'message' => 'Suscripción exitosa.'
        ];
    }

    /**
     * Get subscriber count
     */
    public function count(): int
    {
        return count($this->getAll());
    }
}
