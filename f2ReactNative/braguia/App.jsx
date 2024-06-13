import React from 'react';
import { Provider } from 'react-redux';
import { PersistGate } from 'redux-persist/integration/react';
import { store, persistor } from './src/state/store';

import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {View, Text,StyleSheet} from 'react-native';
import HomeScreen from './src/screens/HomeScreen';
import Login from './src/screens/Login';
import Search from './src/screens/Search';
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


const App = () => {
  return (
    <Provider store={store}>
      <PersistGate loading={<Text>Loading...</Text>} persistor={persistor}>
      <NavigationContainer>
            <Stack.Navigator screenOptions={{headerShown:false}}>
            <Stack.Screen name="Tab" component={TabNavigator} />
            <Stack.Screen name="Login" component={Login} />
            <Stack.Screen name="HomeScreen" component={HomeScreen} />
            <Stack.Screen name="Search" component={Search} />
            </Stack.Navigator>
            </NavigationContainer>
      </PersistGate>
    </Provider>
  );
};

export default App;