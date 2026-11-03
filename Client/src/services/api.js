/**
 * Base HTTP API client for Tekkeyah REST services.
 */
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export async function apiClient(endpoint, { method = 'GET', body, headers = {} } = {}) {
  const config = {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...headers,
    },
  };

  if (body) {
    config.body = JSON.stringify(body);
  }

  const res = await fetch(`${BASE_URL}${endpoint}`, config);
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    throw new Error(errorData.message || `Request failed with status ${res.status}`);
  }
  return await res.json();
}
