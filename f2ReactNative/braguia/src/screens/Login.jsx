import { View, Text, TextInput, Image, StyleSheet, Button, Alert, TouchableOpacity } from 'react-native';
import React, { useState } from 'react'
import {cores, api} from '../var.js';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';
import { useDispatch, useSelector } from 'react-redux'; 
//import { useNavigation } from '@react-navigation/native';
import {setCookies, updateUsername} from '../actions/user.js';



/*
TO-DO:
- Falta gerir a sessao e como gerir
*/

const Login = () => {

  const dispatch = useDispatch();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  
  // usar isto 
  const cookieState = useSelector(state => state.data.cookies.cookieVal); // aceder ao estado!!!!
  const userState = useSelector(state => state.data.user.username);
  

  // actions para adicionar informacao

  // reducers -> guardar / atualizar -> N\ao temos que mexer nisto

  // selectors -> ir buscar

  //dispatch(setCookies(cookies)); // usar isto dentro de um use effect sempre

  //  use effect{
  //} [dispatch , VARIAVEIS] // as vezes

  const handleLogin = async () => {
    try {
      const response = await fetch(api+'login', {
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
      });

      if (response.ok){
          console.log(cookieState);
          console.log("response ok");
          const cookies = response.headers.map['set-cookie'];
          console.log(cookies)
          const csrfTokenMatch = cookies.match(/csrftoken=([^;]+)/);
          const sessionIdMatch = cookies.match(/sessionid=([^;]+)/);
          const csrfToken = csrfTokenMatch ? csrfTokenMatch[0] : null;
          const sessionId = sessionIdMatch ? sessionIdMatch[0] : null;
          console.log(csrfToken);
          console.log(sessionId);

          dispatch(updateUsername(username.trim())); // assim que se guarda
          
          if (csrfTokenMatch.length === 2) {
            // Save cookies in Redux store
            console.log("Saved Cookie");
            dispatch(setCookies(csrfToken + ';' + sessionId));
          }
          console.log(cookieState);
          console.log(userState);


          // verificar no logout se o cookie state est]a vazio 

        // dar navigate para o tab navigator ! navigation.navigate('Landmark', { landmark });
      }
      else {
        Alert.alert('Wrong Credentials');
      }
    } catch (error) {
      Alert.alert('Login Failed', `Error: ${error.message}`);
    }
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