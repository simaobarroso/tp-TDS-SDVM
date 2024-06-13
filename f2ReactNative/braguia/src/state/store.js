import { createStore, combineReducers } from 'redux';
import { persistReducer, persistStore } from 'redux-persist';
import AsyncStorage from '@react-native-async-storage/async-storage';
import dataReducer from './reducers'; // Import your dataReducer here

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
};

const rootReducer = combineReducers({
  data: dataReducer,
});

// Wrap the rootReducer with persistReducer
const persistedReducer = persistReducer(persistConfig, rootReducer);

// Use persistedReducer when creating the store
export const store = createStore(persistedReducer);

export const persistor = persistStore(store);