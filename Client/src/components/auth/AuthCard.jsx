import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Mail, Lock, Eye, EyeOff, User, ArrowRight, ShieldCheck } from 'lucide-react';
import { authService } from '../../services/authService';
import Button from '../common/Button';
import './AuthCard.css';

export default function AuthCard({ onAuthSuccess }) {
  const navigate = useNavigate();
  const [mode, setMode] = useState('SIGN_IN'); // 'SIGN_IN' | 'SIGN_UP'
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [rememberDevice, setRememberDevice] = useState(true);
  const [errorMessage, setErrorMessage] = useState(null);

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    fullName: '',
    role: 'DONOR',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage(null);

    try {
      if (mode === 'SIGN_IN') {
        const user = await authService.login({
          username: formData.username || 'youssef@tekkeyah.org',
          password: formData.password,
          rememberDevice,
        });
        onAuthSuccess?.(user);
        navigate('/giving-hub');
      } else {
        const user = await authService.register({
          fullName: formData.fullName,
          email: formData.username,
          password: formData.password,
          role: formData.role,
        });
        onAuthSuccess?.(user);
        navigate('/giving-hub');
      }
    } catch (err) {
      setErrorMessage(err.message || 'Authentication failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="tk-auth-card">
      {/* Header & Segmented Pill */}
      <div className="tk-auth-card__top">
        <div className="tk-auth-card__titles">
          <h2 className="tk-auth-card__title">
            {mode === 'SIGN_IN' ? 'Welcome to Tekkeyah' : 'Create Account'}
          </h2>
          <p className="tk-auth-card__subtitle">
            {mode === 'SIGN_IN'
              ? 'Enter your credentials to access the relief portal.'
              : 'Join as a donor, volunteer, or partner.'}
          </p>
        </div>

        <div className="tk-auth-card__toggle-pills" role="tablist">
          <button
            type="button"
            role="tab"
            aria-selected={mode === 'SIGN_IN'}
            className={`tk-auth-card__toggle-pill ${mode === 'SIGN_IN' ? 'tk-auth-card__toggle-pill--active' : ''}`}
            onClick={() => setMode('SIGN_IN')}
          >
            Sign In
          </button>
          <button
            type="button"
            role="tab"
            aria-selected={mode === 'SIGN_UP'}
            className={`tk-auth-card__toggle-pill ${mode === 'SIGN_UP' ? 'tk-auth-card__toggle-pill--active' : ''}`}
            onClick={() => setMode('SIGN_UP')}
          >
            Sign Up
          </button>
        </div>
      </div>

      {errorMessage && (
        <div className="tk-auth-card__alert">
          {errorMessage}
        </div>
      )}

      {/* Main Form */}
      <form onSubmit={handleSubmit} className="tk-auth-card__form">
        {mode === 'SIGN_UP' && (
          <div className="tk-auth-card__field">
            <label className="tk-auth-card__label" htmlFor="fullName">
              Full Name
            </label>
            <div className="tk-auth-card__input-wrap">
              <User size={18} className="tk-auth-card__input-icon" />
              <input
                id="fullName"
                name="fullName"
                type="text"
                required
                placeholder="Youssef Al-Qassimi"
                value={formData.fullName}
                onChange={handleChange}
                className="tk-auth-card__input"
              />
            </div>
          </div>
        )}

        <div className="tk-auth-card__field">
          <label className="tk-auth-card__label" htmlFor="username">
            Username or Email
          </label>
          <div className="tk-auth-card__input-wrap">
            <Mail size={18} className="tk-auth-card__input-icon" />
            <input
              id="username"
              name="username"
              type="text"
              required
              placeholder="name@tekkeyah.org"
              value={formData.username}
              onChange={handleChange}
              className="tk-auth-card__input"
            />
          </div>
        </div>

        {mode === 'SIGN_UP' && (
          <div className="tk-auth-card__field">
            <label className="tk-auth-card__label" htmlFor="role">
              User Role
            </label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="tk-auth-card__select"
            >
              <option value="DONOR">Donor (الواهب / المتبرع)</option>
              <option value="VOLUNTEER">Volunteer</option>
              <option value="BENEFICIARY">Beneficiary</option>
            </select>
          </div>
        )}

        <div className="tk-auth-card__field">
          <div className="tk-auth-card__label-row">
            <label className="tk-auth-card__label" htmlFor="password">
              Password
            </label>
            {mode === 'SIGN_IN' && (
              <a href="#forgot" className="tk-auth-card__link">
                Forgot password?
              </a>
            )}
          </div>
          <div className="tk-auth-card__input-wrap">
            <Lock size={18} className="tk-auth-card__input-icon" />
            <input
              id="password"
              name="password"
              type={showPassword ? 'text' : 'password'}
              required
              placeholder="••••••••••••"
              value={formData.password}
              onChange={handleChange}
              className="tk-auth-card__input"
            />
            <button
              type="button"
              className="tk-auth-card__eye-btn"
              onClick={() => setShowPassword(!showPassword)}
              aria-label={showPassword ? 'Hide password' : 'Show password'}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
        </div>

        <label className="tk-auth-card__checkbox-label">
          <input
            type="checkbox"
            checked={rememberDevice}
            onChange={(e) => setRememberDevice(e.target.checked)}
            className="tk-auth-card__checkbox"
          />
          <span>Remember this session</span>
        </label>

        <Button
          type="submit"
          variant="primary"
          size="lg"
          fullWidth
          isLoading={isLoading}
          iconRight={<ArrowRight size={18} />}
        >
          {mode === 'SIGN_IN' ? 'Sign In to Tekkeyah' : 'Create Account'}
        </Button>
      </form>

      {/* Card Footer */}
      <div className="tk-auth-card__footer">
        <div className="tk-auth-card__footer-tag">
          <ShieldCheck size={15} />
          <span>Verified Non-Profit Foundation</span>
        </div>
        <a href="#help" className="tk-auth-card__footer-link">
          Need help?
        </a>
      </div>
    </div>
  );
}
