import { Link, NavLink, useLocation } from 'react-router-dom';
import { HeartHandshake, ArrowRight, User, Settings, LogOut } from 'lucide-react';
import Badge from '../common/Badge';
import { channelLabel } from '../../data/notificationChannels';
import { authService } from '../../services/authService';
import './Navbar.css';

export default function Navbar({ user }) {
  const location = useLocation();

  // Full reload so every page re-reads the (now empty) session
  const handleSignOut = () => {
    authService.logout();
    window.location.assign('/');
  };

  return (
    <header className="tk-navbar-container">
      <div className="tk-navbar">
        {/* Brand Logo */}
        <Link to="/" className="tk-navbar__brand">
          <div className="tk-navbar__logo-icon">
            <HeartHandshake size={22} />
          </div>
          <div className="tk-navbar__brand-text">
            <span className="tk-navbar__brand-title">Tekkeyah</span>
              <span className="tk-navbar__brand-sub">Community Relief & Aid</span>
          </div>
        </Link>

        {/* Navigation Links */}
        <nav className="tk-navbar__links">
          <NavLink
            to="/"
            className={({ isActive }) =>
              `tk-navbar__link ${isActive && location.pathname === '/' ? 'tk-navbar__link--active' : ''}`
            }
          >
            Explore Causes
          </NavLink>
          <a href="#events" className="tk-navbar__link">
            Community Events
          </a>
          <a href="#charter" className="tk-navbar__link">
            Our Promise
          </a>
          <a href="#volunteer" className="tk-navbar__link">
            Volunteer Hub
          </a>
          <NavLink
            to="/giving-hub"
            className={({ isActive }) =>
              `tk-navbar__link ${isActive ? 'tk-navbar__link--active' : ''}`
            }
          >
            Giving Hub
          </NavLink>
        </nav>

        {/* Right Actions */}
        <div className="tk-navbar__actions">
          {user ? (
            <>
              <Badge variant="default" size="sm" className="tk-navbar__whatsapp-badge">
                <span className="tk-navbar__status-dot" />
                {channelLabel(user.channel)} alerts on
              </Badge>

              <Link to="/account" className="tk-navbar__profile-pill" aria-label="Account settings">
                <div className="tk-navbar__avatar">
                  <User size={16} />
                </div>
                <div className="tk-navbar__profile-info">
                  <span className="tk-navbar__profile-name">{user.name}</span>
                  <span className="tk-navbar__profile-role">{user.role}</span>
                </div>
                <Settings size={15} className="tk-navbar__profile-gear" aria-hidden="true" />
              </Link>

              <button
                type="button"
                className="tk-navbar__icon-btn"
                aria-label="Sign out"
                title="Sign out"
                onClick={handleSignOut}
              >
                <LogOut size={18} />
              </button>
            </>
          ) : (
            <>
              <Link to="/auth" className="tk-navbar__auth-link">
                Sign In
              </Link>
              <Link to="/auth" className="tk-navbar__cta-btn">
                <span>Get Involved</span>
                <ArrowRight size={15} />
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
