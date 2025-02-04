import React from 'react';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from './src/state/store';
import { useDispatch, useSelector } from 'react-redux'; 
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {View, Text,StyleSheet} from 'react-native';
import HomeScreen from './src/screens/HomeScreen';
import {setCookies, updateUsername} from '../actions/user.js';
import Login from './src/screens/Login';
import Trail from './src/screens/Trail';
import Search from './src/screens/Search';
import Edges from './src/screens/Edges';
import Pin from './src/screens/Pin';
import Emergency from './src/screens/Emergency';
import TabNavigator from './src/navigator/TabNavigator';
import TabTop from './src/navigator/TopTabNavigator';
import { createStore, applyMiddleware } from 'redux';


// import WrappedApp from './WrappedApp';
//const store2 = createStore(() => [], {}, applyMiddleware());
//console.log(store2);

const Stack = createStackNavigator();
console.log("a");
console.log(store);

const AppNavigator = () => {
  const isLoggedIn = useSelector(state => state.data.authReducer.isLoggedIn);
  console.log("IS LOGGEDIN " + isLoggedIn)

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        {isLoggedIn ? (
          <>
            <Stack.Screen name="Tab" component={TabNavigator} />
            <Stack.Screen name="HomeScreen" component={HomeScreen} />
            <Stack.Screen name="Search" component={Search} />
            <Stack.Screen name="Trail" component={Trail} />
            <Stack.Screen name="Edges" component={Edges} />
            <Stack.Screen name="Pin" component={Pin} />
            <Stack.Screen name="Login" component={Login} />
          </>
        ) : (
          <>
          <Stack.Screen name="Login" component={Login} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
  );
};


const App = () => {
  return (
    <Provider store={store}>
      <PersistGate loading={<Text>Loading...</Text>} persistor={persistor}>
        <AppNavigator />
      </PersistGate>
    </Provider>
  );
};
export default App;