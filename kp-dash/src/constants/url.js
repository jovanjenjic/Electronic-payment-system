export const BASE_URL = 'https://localhost:8765';

// AUTH URLS
export const AUTH_URL = `${BASE_URL}/seller/api/auth`;
export const LOGIN_URL = `${AUTH_URL}/signin`;
export const REGISTER_URL = `${AUTH_URL}/signup`;

// SELERS URLS
export const SELLER_URL = `${BASE_URL}/seller/api`;
export const PAYMENT_TYPE_URL = `${SELLER_URL}/payment`;