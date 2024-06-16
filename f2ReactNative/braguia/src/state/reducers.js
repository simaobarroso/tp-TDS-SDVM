import { combineReducers } from 'redux';
import { UPDATE_USER_DETAILS , SET_COOKIES , ADD_TRIP, SET_DISTANCE,RESET_STATE, LOGIN_SUCCESS, LOGOUT} from '../actions/user';
import {SET_APP_INFO, SET_APP_TRAILS, RESET_APP_DATA, SET_APP_PINS} from '../actions/appData';

const user = (user = { username: '', email: '', date_joined: '', user_type: '' }, action) => {
  switch (action.type) {
      case UPDATE_USER_DETAILS:
          return {
              ...user,
              ...action.payload,
          };
      case RESET_STATE:
          return { username: '', email: '', date_joined: '', user_type: '' }; // Reset the user details to initial values
      default:
          return user;
  }
};

const cookies  = (cookies = { cookieVal: ''}, action) => {
    switch (action.type) {
        case SET_COOKIES:
            return { cookieVal: action.cookieVal }
        case RESET_STATE:
            return { cookieVal: '' }; // Reset the cookieVal to its initial value
        default:
            return cookies;
    }
}

const trips = (trips = { tripsVal: [] }, action) => {
  switch (action.type) {
    case ADD_TRIP:
      return {
        tripsVal: [...trips.tripsVal, action.trip]
      };
    case RESET_STATE:
      return {
        tripsVal: []
      };
    default:
      return trips;
  }
};

const distance = (distance = { distanceVal: 1 }, action) => {
  switch (action.type) {
    case SET_DISTANCE:
      return {
        distanceVal: action.dist
      };
    case RESET_STATE:
      return {
        distanceVal: 1
      };
    default:
      return distance;
  }
};

  
const appData = (state = { appinfo: null, trails: [], pins: [] }, action) => {
  switch (action.type) {
    case SET_APP_INFO:
      return {
        ...state,
        appinfo: action.appinfo
      };
    case SET_APP_TRAILS:
      return {
        ...state,
        trails: action.trails
      };
      case SET_APP_PINS:
        return {
          ...state,
          pins: action.pins
        };
    case RESET_APP_DATA:
      return {
        appinfo: null,
        trails: []
      };
    default:
      return state;
  }
};

const initialState = {
  isLoggedIn: false,
  username: '',
  cookies: ''
};

const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        username: action.payload.username,
        cookies: action.payload.cookies
      };
    case LOGOUT:
      return initialState;
    default:
      return state;
  }
};


export default combineReducers({ user, cookies, trips, appData,distance , authReducer});