import React from 'react'
import Home from '../screens/HomeScreen';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import Search from '../screens/Search';
import Emergency from '../screens/Emergency';
import { StyleSheet } from 'react-native';
import CustomIcon from '../components/Customicon';
import theme from '../theme/theme';
import Icon from 'react-native-vector-icons/MaterialIcons';
import {cores} from '../var';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import { View, Text } from 'react-native';

/*
TO-DO
 IGNORAR ISTO E` SUPOSTO SER UMA TOP BAR NAV MAS NAO FUNCIONA
*/

const Tab = createBottomTabNavigator();

const TabNavigator = () => {
    return (
        <Tab.Navigator screenOptions={{headerShown: false,
            tabBarHideOnKeyboard: true,
            tabBarStyle: styles.tabBarStyle,
        }}>
            <Tab.Screen name="Home" component={Home} options={{
                tabBarIcon: ({focused, color, size}) => (
                <Icon size={20} color="white" name="home" />
                )
            }}/>
            <Tab.Screen name="Search" component={Search} options={{
                tabBarIcon: ({focused, color, size}) => (
                    <Icon size={20} color="white" name="search" />
                    )
            }}/>
            <Tab.Screen name="Emergency" component={Emergency} options={{
                tabBarIcon: ({focused, color, size}) => (
                    <Icon size={20} color="white" name="call" />
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