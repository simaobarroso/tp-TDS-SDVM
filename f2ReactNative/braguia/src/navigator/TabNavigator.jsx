import React from 'react'
import Home from '../screens/HomeScreen';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import Search from '../screens/Search';
import Login from '../screens/Login';
import Settings from '../screens/Settings';
import Emergency from '../screens/Emergency';
import Trail from '../screens/Trail';
import User from '../screens/User';
import History from '../screens/History';
import { StyleSheet } from 'react-native';
import CustomIcon from '../components/Customicon';
import theme from '../theme/theme';
import Icon from 'react-native-vector-icons/MaterialIcons';
import {cores} from '../var';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import { View, Text } from 'react-native';


const Tab = createBottomTabNavigator();


const TabNavigator = () => {
  return (
            <Tab.Navigator screenOptions={{headerShown: false,
                tabBarHideOnKeyboard: true,
                tabBarStyle: styles.tabBarStyle,
                // tabBarBackground: () => (<BlurView overlayColor = '' bluramount={15} style={styles.BluerViewStyles}/>),
            }}>
                <Tab.Screen name="Login" component={Login} options={{
                    tabBarIcon: ({focused, color, size}) => (
                    <Icon size={25} color="white" name="home" />
                    )
                }}/>
                <Tab.Screen name="Home" component={Home} options={{
                    tabBarIcon: ({focused, color, size}) => (
                    <Icon size={25} color="white" name="Home" />
                    )
                }}/>

                <Tab.Screen name="Search" component={Search} options={{
                    tabBarIcon: ({focused, color, size}) => (
                        <Icon size={25} color="white" name="search" />
                        )
                }}/>
                <Tab.Screen name="Emergency" component={Emergency} options={{
                    tabBarIcon: ({focused, color, size}) => (
                        <Icon size={25} color="white" name="call" />
                        )
                }}/>
                <Tab.Screen 
                        name="Trail" 
                        component={Trail}  
                        options={{
                        tabBarIcon: ({ focused, color, size }) => (
                            <Icon size={25} color="white" name="directions-walk" />
                        ), 
                        }} 
                        initialParams={{ trailId: '1' }} // Set initialParams here
                    />
                <Tab.Screen name="User" component={User} options={{
                    tabBarIcon: ({focused, color, size}) => (
                        <Icon size={25} color="white" name="history" />
                        )
                }}/>
                </Tab.Navigator>
  )
}


const styles = StyleSheet.create({
    tabBarStyle: {
      backgroundColor: cores.uminho,
      position: 'absolute',
      height: 60,
      borderTopWidth: 0,
      elevation: 0,
      borderTopColor : 'transparent',
    },
    BluerViewStyles: {
        position: 'absolute',
        height: 60,
        borderTopWidth: 0,
        elevation: 0,
        borderTopColor : 'transparent'
    }
})
export default TabNavigator