import { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LandingPage from './pages/LandingPage';
import AuthPage from './pages/AuthPage';
import GivingHubPage from './pages/GivingHubPage';
import NotFoundPage from './pages/NotFoundPage';
import { authService } from './services/authService';

export default function App() {
  const [user, setUser] = useState(() => {
    const existing = authService.getCurrentUser();
    if (existing) return existing;

    const defaultUser = {
      id: 'usr_9021',
      name: 'Youssef Al-Qassimi',
      email: 'youssef@tekkeyah.org',
      role: 'Donor',
      circleId: '#9021',
      isLoggedIn: true,
    };
    authService.setSession(defaultUser);
    return defaultUser;
  });

  const handleAuthSuccess = (authenticatedUser) => {
    setUser(authenticatedUser);
  };

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LandingPage user={user} />} />
        <Route path="/auth" element={<AuthPage onAuthSuccess={handleAuthSuccess} />} />
        <Route path="/giving-hub" element={<GivingHubPage user={user} />} />
        <Route path="/login" element={<Navigate to="/auth" replace />} />
        <Route path="/signup" element={<Navigate to="/auth" replace />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}
