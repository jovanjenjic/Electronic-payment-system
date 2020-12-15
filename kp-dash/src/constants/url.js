export const BASE_URL = 'https://localhost:8765';

// AUTH URLS
export const AUTH_URL = `${BASE_URL}/seller/api/auth`;
export const LOGIN_URL = `${AUTH_URL}/signin`;
export const REGISTER_URL = `${AUTH_URL}/signup`;

// SELERS URLS
export const SELLER_URL = `${BASE_URL}/seller/api`;
export const PAYMENT_TYPE_URL = `${SELLER_URL}/payment`;

// PAYPAL URL
export const PAYPAL_URL = `${BASE_URL}/pay-pal-service/api`;
export const PAYPAL_PAYMENT_URL = `${PAYPAL_URL}/pay`;
export const EXECUTE_PAYPAL_PAYMENT_URL = `${PAYPAL_PAYMENT_URL}/{paymentId}/success`;
export const CANCEL_PAYPAL_PAYMENT_URL = `${PAYPAL_PAYMENT_URL}/{paymentId}/cancel`;

export const PAYPAL_FORM_URL = `${PAYPAL_URL}/form`;

// PAYPAL SUBSCRIPTION URLS
export const PAYPAL_SUBSCRIPTION = `${PAYPAL_URL}/subscription`;
export const EXECUTE_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/success`;
export const CANCEL_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/cancel`;

// BTC URLS
export const BTC_URL = `${BASE_URL}/bitcoin-service/api`;

// BTC FORM URL
export const BTC_FORM_URL = `${BTC_URL}/getForm`;

// TODO: Later add needed bank info and everything else
