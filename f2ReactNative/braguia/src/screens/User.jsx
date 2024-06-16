import React, {useContext} from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSelector,useDispatch } from 'react-redux';
import Icon from 'react-native-vector-icons/MaterialIcons';
import {resetState} from '../actions/user.js';
import {cores} from '../var.js'
import {setCookies, updateUsername, loginSuccess, logout} from '../actions/user.js';
import { useNavigation } from '@react-navigation/native';



const User = () => {
    const dispatch = useDispatch();

    const navigation = useNavigation();

    const userMetaData = useSelector((state) => state.data.user);
    const {
      username,
      email,
      date_joined,
      user_type,
    } = userMetaData;


    const handleLogout = () => {
      dispatch(logout())
    
      // Perform any additional logout actions if needed, such as navigating to a login screen
      navigation.navigate('Login');
    };
    
    console.log(userMetaData);
  
    const formattedDateJoined = new Date(date_joined).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });

      return (
        <View style={[styles.container, ]}>
            <View style={styles.profileContainer}>
              <View style={styles.backgroundContainer}>
              <Icon size={250} color="white" name="person" />
                <Text style={styles.username}>{username}</Text>
                <Text style={styles.infoTitle}>User type : {user_type}</Text>
              </View>
            </View>
    
            <View style={styles.detailContainer}>
              <View style={styles.detailRow}>
                <Icon name="mail" size={36} style={{color:'black'}} />
                <Text style={[styles.detailText, {color:'black'}]}>E-mail: {email}</Text>
              </View>
              <View style={styles.detailRow}>

              </View>
              <View style={styles.detailRow}>
              <Icon name="calendar-month" size={36}  style={{color:'black'}} />
                <Text style={[styles.detailText, {color:'black'}]}>
                  Member since: {formattedDateJoined}
                </Text>
              </View>
            </View>
    
            <TouchableOpacity style={styles.button} >
              <Text style={styles.buttonText}>Trails history</Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.button} onPress={() => {handleLogout()}}>
              <Text style={styles.buttonText}>Logout!</Text>
            </TouchableOpacity>

        </View>
      );

}

const styles = StyleSheet.create({
    container: {
      justifyContent: 'center',
      alignItems: 'center',
    },
    profileContainer: {
      height: 350,
      width: 400,
      marginBottom: 20,
    },
    backgroundContainer: {
      flex: 1,
      backgroundColor: cores.uminho,
      justifyContent: 'center',
      alignItems: 'center',
    },
    username: {
      marginTop: 10,
      color: '#ffffff',
      fontSize: 21,
      fontWeight: 'bold',
      color: 'white'
    },
    infoTitle: {
      fontSize: 20,
      fontWeight: 'bold',
      color: '#000000',
      marginTop: 10,
      color: 'white'
    },
    userType: {
      color: '#FFFFFF',
      fontSize: 20,
      fontWeight: 'bold',
    },
    detailContainer: {
      marginBottom: 20,
    },
    detailRow: {
      flexDirection: 'row',
      alignItems: 'center',
      marginBottom: 15,
    },
    detailText: {
      paddingLeft: 20,
      fontWeight: 'bold',
      fontSize: 16,
      color: '#000000',
    },
    button: {
      backgroundColor: cores.uminho, // Set your desired background color
      paddingHorizontal: 20,
      paddingVertical: 10,
      marginTop: 10,
      margin:10,
      borderRadius: 30,
      elevation: 1, // Add shadow on Android
      width: '45%',
    },
    buttonText: {
      paddingHorizontal: 20,
      paddingVertical: 10,
      color: 'white',
      textAlign: 'center',
    },
  });

export default User;

