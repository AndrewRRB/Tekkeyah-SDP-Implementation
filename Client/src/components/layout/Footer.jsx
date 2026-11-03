import './Footer.css';

export default function Footer() {
  return (
    <footer className="tk-footer-container">
      <div className="tk-footer">
        <div className="tk-footer__brand-block">
          <div className="tk-footer__brand-name">
            <span>Tekkeyah</span>
          </div>
          <p className="tk-footer__copy">
            © 2024 Tekkeyah Community Foundation. Registered 501(c)(3) Non-Profit Organization. All rights reserved.
          </p>
        </div>

        <nav className="tk-footer__links" aria-label="Legal and governance links">
          <a href="#charter" className="tk-footer__link">Transparency Charter</a>
          <a href="#audits" className="tk-footer__link">Financial Audits</a>
          <a href="#privacy" className="tk-footer__link">Donor Privacy</a>
          <a href="#volunteer-code" className="tk-footer__link">Volunteer Code</a>
          <a href="#terms" className="tk-footer__link">Terms of Service</a>
        </nav>
      </div>
    </footer>
  );
}
