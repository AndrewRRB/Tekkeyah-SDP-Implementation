import AuthHeroPanel from '../components/auth/AuthHeroPanel';
import AuthCard from '../components/auth/AuthCard';
import './AuthPage.css';

export default function AuthPage({ onAuthSuccess }) {
  return (
    <main className="tk-auth-page">
      <div className="tk-auth-page__hero-side">
        <AuthHeroPanel />
      </div>
      <div className="tk-auth-page__form-side">
        <AuthCard onAuthSuccess={onAuthSuccess} />
      </div>
    </main>
  );
}