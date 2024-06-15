export const SET_APP_INFO = 'SET_APP_INFO';
export const updateAppInfo = (appinfo) => ({
  type: SET_APP_INFO,
  appinfo 
});

export const SET_APP_TRAILS = 'SET_APP_TRAILS';
export const setTrails = (trails) => ({
  type: SET_APP_TRAILS,
  trails
});

export const SET_APP_PINS = 'SET_APP_PINS';
export const setPins = (pins) => ({
  type: SET_APP_PINS,
  pins
});

// usa se isto quando se da logout !!!
export const RESET_APP_DATA = 'RESET_APP_DATA';
export const resetAppData = () => ({
  type: RESET_APP_DATA,
});