
/**
 * Helper method for resolving is response ok.
 * @param {Response} response 
 */
export const responseOk = (response) => {
  return response.ok || `${response.status}`.startsWith('2')
};