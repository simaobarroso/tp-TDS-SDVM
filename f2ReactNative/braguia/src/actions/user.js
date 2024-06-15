export const UPDATE_USERNAME = 'UPDATE_USERNAME';
export const updateUsername = (username) => ({
  type: UPDATE_USERNAME,
  username 
});

export const SET_COOKIES = 'SET_COOKIES';
export const setCookies = (cookieVal) => ({
  type: SET_COOKIES,
  cookieVal
});

export const ADD_TRIP = 'ADD_TRIP';
export const addTrip = (trip) => ({
  type: ADD_TRIP,
  trip
});

export const SET_DISTANCE = 'SET_DISTANCE';
export const setDistanceRedux = (dist) => ({
  type: SET_DISTANCE,
  dist
});


export const RESET_STATE = 'RESET_STATE';
export const resetState = () => ({
  type: RESET_STATE,
});

export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const loginSuccess = (username, cookies) => ({
  type: 'LOGIN_SUCCESS',
  payload: { username, cookies },
});

export const LOGOUT = 'LOGOUT';
export const logout = () => ({
  type: 'LOGOUT',
});
