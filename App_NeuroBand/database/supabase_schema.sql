-- NeuroBand Supabase Database Schema
-- Complete schema for emotional well-being and biometric monitoring

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- TABLE: users
-- ============================================
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  email TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  last_login TIMESTAMP WITH TIME ZONE,
  
  -- Profile Information
  first_name TEXT,
  last_name TEXT,
  date_of_birth DATE,
  gender TEXT,
  country TEXT,
  
  -- App Preferences
  theme_preference TEXT DEFAULT 'dark',
  notification_enabled BOOLEAN DEFAULT TRUE,
  haptic_feedback_enabled BOOLEAN DEFAULT TRUE,
  sound_enabled BOOLEAN DEFAULT TRUE,
  
  -- Privacy Settings
  data_sharing_enabled BOOLEAN DEFAULT FALSE,
  analytics_enabled BOOLEAN DEFAULT FALSE
);

-- RLS for users
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Users can view their own data" ON users 
  FOR SELECT USING (auth.uid() = id);
CREATE POLICY "Users can update their own data" ON users 
  FOR UPDATE USING (auth.uid() = id);
CREATE POLICY "Users can delete their own data" ON users 
  FOR DELETE USING (auth.uid() = id);

-- ============================================
-- TABLE: devices (Smart bracelets)
-- ============================================
CREATE TABLE devices (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  device_serial_number TEXT UNIQUE NOT NULL,
  device_model TEXT,
  firmware_version TEXT,
  last_connected TIMESTAMP WITH TIME ZONE,
  battery_level INTEGER CHECK (battery_level >= 0 AND battery_level <= 100),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- RLS for devices
ALTER TABLE devices ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Devices can be viewed by their owner" ON devices 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Devices can be updated by their owner" ON devices 
  FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "Devices can be inserted by authenticated users" ON devices 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Devices can be deleted by their owner" ON devices 
  FOR DELETE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: biometric_data (Real-time biometric data)
-- ============================================
CREATE TABLE biometric_data (
  id BIGSERIAL PRIMARY KEY,
  device_id UUID REFERENCES devices(id) ON DELETE CASCADE,
  timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  heart_rate INTEGER CHECK (heart_rate >= 40 AND heart_rate <= 200),
  respiration_rate DECIMAL(5,2) CHECK (respiration_rate >= 8 AND respiration_rate <= 30),
  body_temperature DECIMAL(4,2) CHECK (body_temperature >= 35 AND body_temperature <= 42),
  energy_level INTEGER CHECK (energy_level >= 0 AND energy_level <= 100),
  gsr_value INTEGER CHECK (gsr_value >= 0 AND gsr_value <= 100),
  hrv INTEGER CHECK (hrv >= 10 AND hrv <= 200),
  blood_oxygen INTEGER CHECK (blood_oxygen >= 70 AND blood_oxygen <= 100),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_biometric_data_device_id ON biometric_data(device_id);
CREATE INDEX idx_biometric_data_timestamp ON biometric_data(timestamp);

-- RLS for biometric_data
ALTER TABLE biometric_data ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Biometric data can be viewed by device owner" ON biometric_data 
  FOR SELECT USING (
    EXISTS (
      SELECT 1 FROM devices 
      WHERE devices.id = biometric_data.device_id 
      AND devices.user_id = auth.uid()
    )
  );
CREATE POLICY "Biometric data can be inserted by device owner" ON biometric_data 
  FOR INSERT WITH CHECK (
    EXISTS (
      SELECT 1 FROM devices 
      WHERE devices.id = biometric_data.device_id 
      AND devices.user_id = auth.uid()
    )
  );

-- ============================================
-- TABLE: emotional_records
-- ============================================
CREATE TABLE emotional_records (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  emotional_state TEXT NOT NULL, -- 'calm', 'stressed', 'happy', 'anxious', 'focused', etc.
  stress_level INTEGER CHECK (stress_level >= 0 AND stress_level <= 100),
  energy_level INTEGER CHECK (energy_level >= 0 AND energy_level <= 100),
  mood_score INTEGER CHECK (mood_score >= 0 AND mood_score <= 100),
  notes TEXT,
  context TEXT, -- 'work', 'exercise', 'rest', 'social', etc.
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_emotional_records_user_id ON emotional_records(user_id);
CREATE INDEX idx_emotional_records_timestamp ON emotional_records(timestamp);

-- RLS for emotional_records
ALTER TABLE emotional_records ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Emotional records can be viewed by owner" ON emotional_records 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Emotional records can be inserted by owner" ON emotional_records 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Emotional records can be updated by owner" ON emotional_records 
  FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "Emotional records can be deleted by owner" ON emotional_records 
  FOR DELETE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: ai_predictions
-- ============================================
CREATE TABLE ai_predictions (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  prediction_type TEXT NOT NULL, -- 'stress', 'energy', 'sleep', 'emotional'
  predicted_value DECIMAL(5,2),
  confidence_level DECIMAL(3,2) CHECK (confidence_level >= 0 AND confidence_level <= 1),
  prediction_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  model_version TEXT,
  features_used JSONB,
  is_accurate BOOLEAN,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_ai_predictions_user_id ON ai_predictions(user_id);
CREATE INDEX idx_ai_predictions_timestamp ON ai_predictions(prediction_timestamp);

-- RLS for ai_predictions
ALTER TABLE ai_predictions ENABLE ROW LEVEL SECURITY;
CREATE POLICY "AI predictions can be viewed by owner" ON ai_predictions 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "AI predictions can be inserted by system" ON ai_predictions 
  FOR INSERT WITH CHECK (auth.uid() = user_id);

-- ============================================
-- TABLE: recommendations
-- ============================================
CREATE TABLE recommendations (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  recommendation_type TEXT NOT NULL, -- 'breathing', 'meditation', 'break', 'exercise', 'sleep'
  title TEXT NOT NULL,
  description TEXT,
  priority TEXT DEFAULT 'medium', -- 'low', 'medium', 'high'
  is_completed BOOLEAN DEFAULT FALSE,
  scheduled_at TIMESTAMP WITH TIME ZONE,
  completed_at TIMESTAMP WITH TIME ZONE,
  ai_generated BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_recommendations_user_id ON recommendations(user_id);
CREATE INDEX idx_recommendations_scheduled ON recommendations(scheduled_at);

-- RLS for recommendations
ALTER TABLE recommendations ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Recommendations can be viewed by owner" ON recommendations 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Recommendations can be inserted by system" ON recommendations 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Recommendations can be updated by owner" ON recommendations 
  FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "Recommendations can be deleted by owner" ON recommendations 
  FOR DELETE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: notifications
-- ============================================
CREATE TABLE notifications (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  title TEXT NOT NULL,
  message TEXT NOT NULL,
  type TEXT NOT NULL, -- 'alert', 'info', 'success', 'warning'
  is_read BOOLEAN DEFAULT FALSE,
  action_required BOOLEAN DEFAULT FALSE,
  action_url TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  read_at TIMESTAMP WITH TIME ZONE
);

-- Index for performance
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);

-- RLS for notifications
ALTER TABLE notifications ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Notifications can be viewed by owner" ON notifications 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Notifications can be inserted by system" ON notifications 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Notifications can be updated by owner" ON notifications 
  FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "Notifications can be deleted by owner" ON notifications 
  FOR DELETE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: settings
-- ============================================
CREATE TABLE settings (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE UNIQUE,
  vibration_intensity INTEGER DEFAULT 50 CHECK (vibration_intensity >= 0 AND vibration_intensity <= 100),
  stress_sensitivity TEXT DEFAULT 'medium', -- 'low', 'medium', 'high'
  therapeutic_sounds_enabled BOOLEAN DEFAULT TRUE,
  smart_alerts_enabled BOOLEAN DEFAULT TRUE,
  calm_modes JSONB, -- Store calm mode configurations
  automatic_routines JSONB, -- Store automatic routine configurations
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- RLS for settings
ALTER TABLE settings ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Settings can be viewed by owner" ON settings 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Settings can be inserted by owner" ON settings 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Settings can be updated by owner" ON settings 
  FOR UPDATE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: sessions
-- ============================================
CREATE TABLE sessions (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  device_id UUID REFERENCES devices(id) ON DELETE SET NULL,
  session_type TEXT NOT NULL, -- 'breathing', 'meditation', 'monitoring', 'sleep'
  start_time TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  end_time TIMESTAMP WITH TIME ZONE,
  duration_seconds INTEGER,
  average_heart_rate INTEGER,
  average_stress_level INTEGER,
  notes TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_sessions_user_id ON sessions(user_id);
CREATE INDEX idx_sessions_start_time ON sessions(start_time);

-- RLS for sessions
ALTER TABLE sessions ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Sessions can be viewed by owner" ON sessions 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Sessions can be inserted by owner" ON sessions 
  FOR INSERT WITH CHECK (auth.uid() = user_id);
CREATE POLICY "Sessions can be updated by owner" ON sessions 
  FOR UPDATE USING (auth.uid() = user_id);
CREATE POLICY "Sessions can be deleted by owner" ON sessions 
  FOR DELETE USING (auth.uid() = user_id);

-- ============================================
-- TABLE: privacy_logs
-- ============================================
CREATE TABLE privacy_logs (
  id BIGSERIAL PRIMARY KEY,
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  action_type TEXT NOT NULL, -- 'data_access', 'data_export', 'data_deletion', 'consent_update'
  description TEXT,
  ip_address TEXT,
  user_agent TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance
CREATE INDEX idx_privacy_logs_user_id ON privacy_logs(user_id);
CREATE INDEX idx_privacy_logs_created_at ON privacy_logs(created_at);

-- RLS for privacy_logs
ALTER TABLE privacy_logs ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Privacy logs can be viewed by owner" ON privacy_logs 
  FOR SELECT USING (auth.uid() = user_id);
CREATE POLICY "Privacy logs can be inserted by system" ON privacy_logs 
  FOR INSERT WITH CHECK (auth.uid() = user_id);

-- ============================================
-- FUNCTIONS AND TRIGGERS
-- ============================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply trigger to tables with updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_devices_updated_at BEFORE UPDATE ON devices
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_settings_updated_at BEFORE UPDATE ON settings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- VIEWS FOR COMMON QUERIES
-- ============================================

-- View for user dashboard summary
CREATE VIEW user_dashboard_summary AS
SELECT 
    u.id,
    u.email,
    u.first_name,
    u.last_name,
    COUNT(DISTINCT d.id) as device_count,
    COUNT(DISTINCT bd.id) as biometric_readings_count,
    MAX(bd.timestamp) as last_reading,
    AVG(bd.heart_rate) as avg_heart_rate,
    AVG(bd.stress_level) as avg_stress_level
FROM users u
LEFT JOIN devices d ON u.id = d.user_id AND d.is_active = true
LEFT JOIN biometric_data bd ON d.id = bd.device_id
GROUP BY u.id;

-- View for emotional trends
CREATE VIEW emotional_trends AS
SELECT 
    user_id,
    DATE_TRUNC('day', timestamp) as date,
    AVG(stress_level) as avg_stress,
    AVG(energy_level) as avg_energy,
    AVG(mood_score) as avg_mood,
    COUNT(*) as record_count
FROM emotional_records
GROUP BY user_id, DATE_TRUNC('day', timestamp)
ORDER BY date DESC;
