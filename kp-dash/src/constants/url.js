export const BASE_URL = 'https://localhost:8765';

export const LU_URL = 'https://localhost:8090';

// AUTH URLS
export const AUTH_URL = `${LU_URL}/auth`;
export const LOGIN_URL = `${AUTH_URL}/login`;
export const REGISTER_URL = `${AUTH_URL}/register`;

// SELERS URLS
// export const SELLER_URL = `${BASE_URL}/seller/api`;
// export const PAYMENT_TYPE_URL = `${SELLER_URL}/payment`;
export const PAYMENT_TYPE_URL = `${LU_URL}/payment/types`;

// PAYPAL URL
export const PAYPAL_URL = `${BASE_URL}/pay-pal-service/api`;
// export const PAYPAL_PAYMENT_URL = `${PAYPAL_URL}/pay`;
// export const PAYPAL_ADD_PAYMENT_URL = `${PAYPAL_URL}/addPayment`;
// export const EXECUTE_PAYPAL_PAYMENT_URL = `${PAYPAL_PAYMENT_URL}/{paymentId}/success`;
// export const CANCEL_PAYPAL_PAYMENT_URL = `${PAYPAL_PAYMENT_URL}/{paymentId}/cancel`;
export const PAYPAL_PAYMENT_URL = `${LU_URL}/payment/order`;
export const PAYPAL_ADD_PAYMENT_URL = `${LU_URL}/payment/paypal/register`;
export const EXECUTE_PAYPAL_PAYMENT_URL = `${LU_URL}/payment/paypal/{paymentId}/execute`;
export const CANCEL_PAYPAL_PAYMENT_URL = `${LU_URL}/payment/paypal/{paymentId}/cancel`;

export const PAYPAL_FORM_URL = `${PAYPAL_URL}/form`;

// PAYPAL SUBSCRIPTION URLS
// export const PAYPAL_SUBSCRIPTION = `${PAYPAL_URL}/subscription`;
// export const EXECUTE_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/success`;
// export const CANCEL_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/cancel`;

export const PAYPAL_SUBSCRIPTION = `${LU_URL}/payment/paypal/subscription`;
export const EXECUTE_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/execute`;
export const CANCEL_PAYPAL_SUBSCRIPTION = `${PAYPAL_SUBSCRIPTION}/{subscriptionId}/cancel`;

// BTC URLS
export const BTC_URL = `${BASE_URL}/bitcoin-service/api`;

// BTC FORM URL
export const BTC_FORM_URL = `${BTC_URL}/form`;
export const BTC_ADD_PAYMENT_URL = `${LU_URL}/payment/btc/register`;
export const BTC_CREATE_PAYMENT_URL = `${LU_URL}/payment/order`;
export const BTC_SUCCESS_PAYMENT_URL = `${LU_URL}/payment/btc/transaction/update`;
export const BTC_CANCEL_PAYMENT_URL = `${LU_URL}/payment/btc/transaction/update`;

// TODO: Later add needed bank info and everything else
export const BANK_URL = `${BASE_URL}/bank-service/api`;

// export const BANK_ADD_PAYMENT_URL = `${BANK_URL}/merchant/`;
// export const BANK_CREATE_PAYMENT_URL = `${BANK_ADD_PAYMENT_URL}create`;
export const BANK_ADD_PAYMENT_URL = `${LU_URL}/payment/bank/register`;
export const BANK_CREATE_PAYMENT_URL = `${LU_URL}/payment/order`;
export const BANK_PAYMENT_SUCCESS_URL = `${LU_URL}/payment/bank/{paymentId}/success`;
export const BANK_PAYMENT_CANCEL_URL = `${LU_URL}/payment/bank/{paymentId}/cancel`;
export const BANK_PAYMENT_FAILED_URL = `${LU_URL}/payment/bank/{paymentId}/failed`;


export const ITEMS_URL = `${LU_URL}/item`;
export const ALL_ITEMS_URL = `${ITEMS_URL}/all`;
