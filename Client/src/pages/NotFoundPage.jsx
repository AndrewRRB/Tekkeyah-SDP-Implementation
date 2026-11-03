import { Link } from 'react-router-dom';
import { Home } from 'lucide-react';
import Button from '../components/common/Button';
import './NotFoundPage.css';

export default function NotFoundPage() {
  return (
    <div className="tk-not-found-page">
      <div className="tk-not-found-card">
        <span className="tk-not-found-code">404</span>
        <h2 className="tk-not-found-title">Page Not Found</h2>
        <p className="tk-not-found-desc">
          The requested page is not in the Tekkeyah registry or may have been relocated.
        </p>
        <Link to="/">
          <Button variant="primary" size="md" iconLeft={<Home size={16} />}>
            Return to Tekkeyah Home
          </Button>
        </Link>
      </div>
    </div>
  );
}
