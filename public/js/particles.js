/* ──────────────────────────────────────────────────────────────
   particles.js – Canvas particle system for the hero section
   NeuroBrand Landing Page
   ────────────────────────────────────────────────────────────── */

class ParticleSystem {
  constructor(canvasId) {
    this.canvas = document.getElementById(canvasId);
    if (!this.canvas) return;

    this.ctx = this.canvas.getContext('2d');
    this.particles = [];
    this.dpr = Math.min(window.devicePixelRatio || 1, 2);
    this.connectionDistance = 140;
    this.mouseInfluenceRadius = 220;

    this.colors = [
      { r: 175, g: 203, b: 255, a: 0.40 },  // blue
      { r: 183, g: 245, b: 209, a: 0.30 },  // mint
      { r: 220, g: 203, b: 255, a: 0.35 },  // lavender
      { r: 255, g: 215, b: 180, a: 0.25 },  // warm peach accent
    ];

    this.mouse = { x: null, y: null };
    this.frameId = null;
    this.isVisible = true;
    this.lastTime = 0;

    this._onResize = this._handleResize.bind(this);
    this._onMouse = this._handleMouse.bind(this);
    this._onMouseLeave = this._handleMouseLeave.bind(this);
    this._onVisibility = this._handleVisibility.bind(this);

    this.init();
  }

  /* ── Setup ─────────────────────────────────────────────────── */

  init() {
    this._setCanvasSize();
    this._createParticles();
    this._bindEvents();
    this._animate(0);
  }

  _getParticleCount() {
    const w = window.innerWidth;
    if (w < 480) return 20;
    if (w < 768) return 30;
    if (w < 1200) return 45;
    return 60;
  }

  _setCanvasSize() {
    const rect = this.canvas.parentElement.getBoundingClientRect();
    this.width = rect.width;
    this.height = rect.height;
    this.canvas.width = this.width * this.dpr;
    this.canvas.height = this.height * this.dpr;
    this.canvas.style.width = this.width + 'px';
    this.canvas.style.height = this.height + 'px';
    this.ctx.scale(this.dpr, this.dpr);
  }

  _createParticles() {
    this.particles = [];
    const count = this._getParticleCount();
    for (let i = 0; i < count; i++) {
      this.particles.push(this._createParticle());
    }
  }

  _createParticle() {
    const color = this.colors[Math.floor(Math.random() * this.colors.length)];
    const radius = 2 + Math.random() * 4; // 2-6px
    return {
      x: Math.random() * this.width,
      y: Math.random() * this.height,
      radius: radius,
      baseRadius: radius,
      color: color,
      // Gentle drift velocities
      vx: (Math.random() - 0.5) * 0.3,
      vy: (Math.random() - 0.5) * 0.3,
      // Sin/cos oscillation params
      sinOffset: Math.random() * Math.PI * 2,
      sinSpeed: 0.0005 + Math.random() * 0.001,
      sinAmplitude: 15 + Math.random() * 25,
      cosOffset: Math.random() * Math.PI * 2,
      cosSpeed: 0.0003 + Math.random() * 0.0008,
      cosAmplitude: 10 + Math.random() * 20,
      // Base positions for oscillation
      baseX: 0,
      baseY: 0,
      // Opacity pulsing
      pulseOffset: Math.random() * Math.PI * 2,
      pulseSpeed: 0.001 + Math.random() * 0.002,
    };
  }

  /* ── Events ────────────────────────────────────────────────── */

  _bindEvents() {
    window.addEventListener('resize', this._onResize, { passive: true });
    this.canvas.addEventListener('mousemove', this._onMouse, { passive: true });
    this.canvas.addEventListener('mouseleave', this._onMouseLeave, { passive: true });
    // Touch support
    this.canvas.addEventListener('touchmove', this._onMouse, { passive: true });
    this.canvas.addEventListener('touchend', this._onMouseLeave, { passive: true });
    document.addEventListener('visibilitychange', this._onVisibility, { passive: true });
  }

  _handleResize() {
    this._setCanvasSize();
    // Adjust particle count
    const targetCount = this._getParticleCount();
    while (this.particles.length < targetCount) {
      this.particles.push(this._createParticle());
    }
    while (this.particles.length > targetCount) {
      this.particles.pop();
    }
    // Clamp existing particles into new bounds
    for (const p of this.particles) {
      p.x = Math.min(p.x, this.width);
      p.y = Math.min(p.y, this.height);
    }
  }

  _handleMouse(e) {
    const rect = this.canvas.getBoundingClientRect();
    if (e.touches && e.touches.length > 0) {
      this.mouse.x = e.touches[0].clientX - rect.left;
      this.mouse.y = e.touches[0].clientY - rect.top;
    } else {
      this.mouse.x = e.clientX - rect.left;
      this.mouse.y = e.clientY - rect.top;
    }
  }

  _handleMouseLeave() {
    this.mouse.x = null;
    this.mouse.y = null;
  }

  _handleVisibility() {
    this.isVisible = !document.hidden;
    if (this.isVisible) {
      this.lastTime = performance.now();
      this._animate(this.lastTime);
    }
  }

  /* ── Animation Loop ────────────────────────────────────────── */

  _animate(timestamp) {
    if (!this.isVisible) return;

    this.frameId = requestAnimationFrame(this._animate.bind(this));

    const delta = timestamp - this.lastTime;
    this.lastTime = timestamp;

    // Cap delta to prevent huge jumps when tab regains focus
    const dt = Math.min(delta, 50);

    this.ctx.clearRect(0, 0, this.width, this.height);

    this._updateParticles(timestamp, dt);
    this._drawConnections();
    this._drawParticles(timestamp);
  }

  _updateParticles(time, dt) {
    for (const p of this.particles) {
      // Gentle drift
      p.x += p.vx * (dt * 0.06);
      p.y += p.vy * (dt * 0.06);

      // Sin/cos floating motion
      const sinVal = Math.sin(time * p.sinSpeed + p.sinOffset) * p.sinAmplitude * 0.01;
      const cosVal = Math.cos(time * p.cosSpeed + p.cosOffset) * p.cosAmplitude * 0.01;
      p.x += sinVal * (dt * 0.06);
      p.y += cosVal * (dt * 0.06);

      // Mouse parallax (gentle push away)
      if (this.mouse.x !== null && this.mouse.y !== null) {
        const dx = p.x - this.mouse.x;
        const dy = p.y - this.mouse.y;
        const dist = Math.sqrt(dx * dx + dy * dy);
        if (dist < this.mouseInfluenceRadius && dist > 0) {
          const force = (1 - dist / this.mouseInfluenceRadius) * 0.8;
          p.x += (dx / dist) * force * (dt * 0.06);
          p.y += (dy / dist) * force * (dt * 0.06);
        }
      }

      // Wrap around edges with padding
      const pad = 20;
      if (p.x < -pad) p.x = this.width + pad;
      if (p.x > this.width + pad) p.x = -pad;
      if (p.y < -pad) p.y = this.height + pad;
      if (p.y > this.height + pad) p.y = -pad;
    }
  }

  _drawParticles(time) {
    for (const p of this.particles) {
      // Pulse opacity
      const pulse = 0.7 + 0.3 * Math.sin(time * p.pulseSpeed + p.pulseOffset);
      const alpha = p.color.a * pulse;

      // Soft glow (larger, more transparent circle behind)
      this.ctx.beginPath();
      this.ctx.arc(p.x, p.y, p.radius * 2.5, 0, Math.PI * 2);
      this.ctx.fillStyle = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, ${alpha * 0.15})`;
      this.ctx.fill();

      // Main particle
      this.ctx.beginPath();
      this.ctx.arc(p.x, p.y, p.radius, 0, Math.PI * 2);
      this.ctx.fillStyle = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, ${alpha})`;
      this.ctx.fill();
    }
  }

  _drawConnections() {
    const len = this.particles.length;
    for (let i = 0; i < len; i++) {
      for (let j = i + 1; j < len; j++) {
        const a = this.particles[i];
        const b = this.particles[j];
        const dx = a.x - b.x;
        const dy = a.y - b.y;
        const dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < this.connectionDistance) {
          const opacity = (1 - dist / this.connectionDistance) * 0.08;
          // Blend colors of the two particles
          const r = Math.round((a.color.r + b.color.r) / 2);
          const g = Math.round((a.color.g + b.color.g) / 2);
          const bVal = Math.round((a.color.b + b.color.b) / 2);

          this.ctx.beginPath();
          this.ctx.moveTo(a.x, a.y);
          this.ctx.lineTo(b.x, b.y);
          this.ctx.strokeStyle = `rgba(${r}, ${g}, ${bVal}, ${opacity})`;
          this.ctx.lineWidth = 0.8;
          this.ctx.stroke();
        }
      }
    }
  }

  /* ── Cleanup ───────────────────────────────────────────────── */

  destroy() {
    if (this.frameId) cancelAnimationFrame(this.frameId);
    window.removeEventListener('resize', this._onResize);
    this.canvas.removeEventListener('mousemove', this._onMouse);
    this.canvas.removeEventListener('mouseleave', this._onMouseLeave);
    this.canvas.removeEventListener('touchmove', this._onMouse);
    this.canvas.removeEventListener('touchend', this._onMouseLeave);
    document.removeEventListener('visibilitychange', this._onVisibility);
    this.particles = [];
  }
}
