import { View, Text, TextInput, Image, StyleSheet, Button, Alert, TouchableOpacity } from 'react-native';
import React, { useState, useEffect } from 'react'
import {cores, api} from '../var.js';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import { useDispatch, useSelector } from 'react-redux'; 
//import { useNavigation } from '@react-navigation/native';
import {setCookies, updateUsername, loginSuccess, logout} from '../actions/user.js';
import {setTrails, updateAppInfo, setPins} from '../actions/appData.js';
import { useNavigation } from '@react-navigation/native';
import HomeScreen from './HomeScreen.jsx'

/*
TO-DO:
- Falta gerir a sessao e como gerir
*/

const Login = () => {

  const dispatch = useDispatch();
  const navigation = useNavigation();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);


  const cookieState = useSelector(state => state.data.cookies.cookieVal);
  const userState   = useSelector(state => state.data.user.username);


  const getAppInfo = async () => {
    try {
      const response = await fetch(api + 'app');
      if (response.ok) {
        const data = await response.json();
  
        dispatch(updateAppInfo(data[0]));
        console.log('App Info:', data[0]);
      } else {
        // Dispatch an error message if the response is not ok
        console.log("Error fetching app info!!");
        dispatch(updateAppInfo("Error fetching app info"));
      }
    } catch (error) {
      // Log and dispatch an error message in case of an exception
      console.log("Error fetching data!!!", error);
      dispatch(updateAppInfo("Error fetching app info"));
    }
  };

  const getTrails = async () => {
      try{
          const response = await fetch(api + 'trails');
          if (response.ok) {
              const data = await response.json();
              dispatch(setTrails(data));
          }
      }
      catch (error) {
          dispatch(setTrails("Error fetchin g trails:", error));
      }
  }

  const getPins = async () => {
    try{
        const response = await fetch(api + 'pins');
        if (response.ok) {
            const data = await response.json();
            dispatch(setPins(data));
        }
    }
    catch (error) {
        dispatch(setPins("Error fetchin pins:", error));
    }
}

  const getUserFunc = () => {
    fetch(api + 'user', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Cookie': cookieState
      },
    })
    .then(response => {
      if (response.ok) {
        // Perform any additional logout actions
        return response.json();
      } else {
        console.log('GetUser request failed:', response.status);
        throw new Error('GetUser request failed');
      }
    })
    .then(user => {
      dispatch(updateUsername(user));
    })
    .catch(error => {
      console.log('GetUser request failed by error:', error);
      throw new Error('GetUser request failed');
    });
  };


  useEffect(() => {
    getAppInfo();
    getTrails();
    getPins();
  }, []);

  const handleLogin = () => {
    fetch(api + 'login', {
      credentials: 'omit',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        username: username.trim(),
        email: "",
        password: password,
      }),
    })
      .then(response => {
        if (!response.ok) {
          Alert.alert('Wrong Credentials');
          throw new Error('Wrong Credentials');
        }
        return response;
      })
      .then(response => response.headers)
      .then(headers => {
        const cookies = headers.map['set-cookie'];
        const csrfTokenMatch = cookies.match(/csrftoken=([^;]+)/);
        const sessionIdMatch = cookies.match(/sessionid=([^;]+)/);
        const csrfToken = csrfTokenMatch ? csrfTokenMatch[0] : null;
        const sessionId = sessionIdMatch ? sessionIdMatch[0] : null;

        dispatch(updateUsername(username.trim())); // Update the username in the store

        if (csrfTokenMatch && sessionIdMatch) {
          // Save cookies in Redux store
          dispatch(setCookies(csrfToken + ';' + sessionId));
        }

        dispatch(loginSuccess(username.trim(), csrfToken + ';' + sessionId));

        

        // Navigate to HomeScreen upon successful login
        navigation.navigate('HomeScreen');
      })
      .catch(error => {
        Alert.alert('Login Failed', `Error: ${error.message}`);
      });
  };




  const toggleShowPassword = () => {
    setShowPassword(!showPassword);
  };
  

  return (
    <View style={styles.container}>
    <Image source={require('../../assets/logo_red.png')} style={styles.image} />
    <Text style={styles.title}>Login</Text>
    <TextInput 
      style={styles.input} 
      placeholder="Username"
      placeholderTextColor="#D3D3D3"  
      onChangeText={setUsername}
    />
    <View style={styles.inputContainer}>
      <TextInput 
        style={styles.input} 
        placeholder="Password" 
        secureTextEntry={!showPassword}
        placeholderTextColor="#D3D3D3"  
        value={password}
        onChangeText={setPassword}
      />
      <TouchableOpacity onPress={toggleShowPassword} style={styles.eyeIcon}>
        <Icon name={showPassword ? 'eye-off' : 'eye'} size={24} color="black" />
      </TouchableOpacity>

  </View>

      <TouchableOpacity style={styles.button} onPress={handleLogin}>
        <Text>Login!</Text>
      </TouchableOpacity>
  </View>
  )
}


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


export default Login