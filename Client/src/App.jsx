import { useCallback, useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import AuthPage from './pages/AuthPage';
import GivingHubPage from './pages/GivingHubPage';
import NotFoundPage from './pages/NotFoundPage';
import AccountSettingsPage from './pages/AccountSettingsPage';
import { authService } from './services/authService';

// Only sessions returned by the backend (numeric user id) are valid.
const loadSession = () => {
  const existing = authService.getCurrentUser();
  if (existing && Number.isInteger(existing.id)) return existing;
  if (existing) authService.logout();
  return null;
};

export default function App() {
  const [user, setUser] = useState(loadSession);

  const handleAuthSuccess = (authenticatedUser) => {
    setUser(authenticatedUser);
  };

  // Stable so the settings page can use it in effects
  const handleUserUpdated = useCallback((freshUser) => {
    authService.setSession(freshUser);
    setUser(freshUser);
  }, []);

  const handleSignedOut = useCallback(() => {
    authService.logout();
    setUser(null);
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage user={user} />} />
        <Route path="/auth" element={<AuthPage onAuthSuccess={handleAuthSuccess} />} />
        <Route
          path="/giving-hub"
          element={user ? <GivingHubPage user={user} /> : <Navigate to="/auth" replace />}
        />
        <Route
          path="/account"
          element={
            user ? (
              <AccountSettingsPage user={user} onUserUpdated={handleUserUpdated} onSignedOut={handleSignedOut} />
            ) : (
              <Navigate to="/auth" replace />
            )
          }
        />
        <Route path="/login" element={<Navigate to="/auth" replace />} />
        <Route path="/signup" element={<Navigate to="/auth" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}
