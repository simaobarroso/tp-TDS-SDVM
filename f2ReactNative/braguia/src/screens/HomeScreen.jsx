import { View, Text, Image  } from 'react-native';
import { StyleSheet } from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'

const HomeScreen = () => {

    const [title, setTitle] = useState("Title");
    const [appInfo, setAppInfo] = useState("Loading");
  
    const getTitle = async () => {
      try {
        const response = await fetch(api + 'app');
        if (response.ok) {
          const data = await response.json();
          setTitle(data.title);
          setAppInfo(data.appInfo);
        } else {
          setAppInfo("Error fetching data");
        }
      } catch (error) {
        setAppInfo("Error fetching data");
      }
    };
  
    useEffect(() => {
      getTitle();
    }, []);
  
    return (
      <View style={styles.container}>
        <Text style={styles.title}>{title}</Text>
        <Text >{appInfo}</Text>
      </View>
    );
};

const styles = StyleSheet.create({
    button: {
      backgroundColor: cores.uminho, // Set your desired background color
      paddingHorizontal: 20,
      paddingVertical: 10,
      borderRadius: 5,
      elevation: 3, // Add shadow on Android
    },
    inputContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      borderBottomWidth: 1,
      borderBottomColor: 'white',
      marginBottom: 10,
    },
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      padding: 20,
      backgroundColor : 'white',
  
    },
    image: {
      width: 200,  // Set the desired width
      height: 200, // Set the desired height
      marginBottom: 20,
      resizeMode: 'contain', // This ensures the image is scaled to fit the container
    },
    title: {
      fontSize: 24,
      fontWeight: 'bold',
      marginBottom: 20,
      color : 'black'
    },
    input: {
      width: '80%',
      height: 40,
      borderColor: 'gray',
      borderWidth: 1,
      marginBottom: 20,
      paddingHorizontal: 20,
      color : 'black',
      borderRadius: 5,
    },
    eyeIcon: {
      position: 'absolute',
      right: 10,
      top:6
    },
    
  });
export default HomeScreen;