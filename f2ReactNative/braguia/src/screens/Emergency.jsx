import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, Linking } from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js';
import Icon from 'react-native-vector-icons/MaterialIcons';


/* TO-DO:

- Melhorar interface

*/

const Emergency = () => {

    const appinfo = useSelector(state => state.data.appData.appinfo.app_desc);

    const contacts = useSelector(state => state.data.appData.appinfo.contacts);

    // <Image source={{ uri: item.image }} style={styles.trailImage} />

    const renderPhone = ({ item }) => (
        <View >
            <TouchableOpacity style={{ 
                        flexDirection: 'row',
                        alignItems: 'center',
                        borderWidth: 1,           // Add border width
                        borderColor: 'lightgray', // Set border color to light gray
                        borderRadius: 5,          // Optional: Add border radius for rounded corners
                        padding: 10,       

             }} onPress={() => Linking.openURL(`tel:${item.contact_phone}`)}>
            <Icon name="call" size={25} color="#000000" style={{color:'black'}} />
            <Text style={styles.title2}>{item.contact_phone}</Text>
            </TouchableOpacity>
      </View>

    );

    const renderEmail= ({ item }) => (
        <View >
        <TouchableOpacity style={{ 
          flexDirection: 'row',
          alignItems: 'center',
          borderWidth: 1,           // Add border width
          borderColor: 'lightgray', // Set border color to light gray
          borderRadius: 5,          // Optional: Add border radius for rounded corners
          padding: 10,              // Optional: Add padding inside the container

        }} onPress={() => Linking.openURL(`mailto:${item.contact_mail}`)}>
        <Icon name="alternate-email" size={25} color="#000000" style={{color:'black'}} />

          <Text style={styles.title2}>{item.contact_mail}</Text>
        </TouchableOpacity>
      </View>
    );


  
    return (

       <View style={styles.container}>
          <View style={styles.topBar}>
            <Image source={require('../../assets/logo_white.png')} style={styles.logo} />
            <Text style={styles.title}>{appinfo}</Text>
          
          </View>
            <View style={styles.container2}>

          <Text style={styles.outTrails}>Phone</Text>
          <FlatList
          data={contacts}
          renderItem={renderPhone}
          keyExtractor={(item) => item.contact_phone}
          vertical
          showsHorizontalScrollIndicator={false}
        />
         </View>


         <View style={styles.container2}>
        <Text style={styles.outTrails}>Email</Text>
        <FlatList
            data={contacts}
            renderItem={renderEmail}
            keyExtractor={(item) => item.contact_mail}
            vertical
            showsHorizontalScrollIndicator={false}
          />
          </View>


      </View>
    );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 0,
    backgroundColor : 'white'

  }
  ,
  title2: {
    paddingVertical: 15,
    paddingHorizontal: 10,
    fontSize: 18,
    fontWeight: 'bold',
    color : 'black'
  },
  container2: {
    width: '80%', // Adjust width as needed
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor : 'white',
    padding: 10,
  },
  topBar: {
    flexDirection: 'row',
    backgroundColor: cores.uminho, 
    alignItems: 'center',
    paddingHorizontal: 12,
    height: 70, 
    width: '100%',
    marginBottom: 20
  },
  title: {
    paddingVertical: 15,
    paddingHorizontal: 10,
    fontSize: 18,
    fontWeight: 'bold',
    color : 'white'
  },
  logo: {
    paddingVertical: 15,
    width: 30,
    height: 40,
    borderRadius: 8
  },
  outTrails: {
    fontSize: 35,
    fontWeight: 'bold',
    marginBottom: 30,
    color : 'black',
    textAlign: 'left'
  },
  appDesc: {
    fontSize: 16,
    fontWeight: 'normal',
    marginBottom: 290,
    color : 'black'
  },
  trailItem: {
      marginRight: 16,
      alignItems: 'center',
      height: 200,
      backgroundColor: '#FFF7F7',
      borderRadius: 15,
      elevation: 4
    },
    trailImage: {
      width: 150,
      height: 150,
      borderTopLeftRadius: 15,
      borderTopRightRadius: 15
    },
    trailName: {
      marginTop: 8,
      fontSize: 14,
      color : 'black',
      fontWeight: '600'
    },
    trailDescContainer: {
      marginTop: 2,
      flexDirection: 'row',
      justifyContent: 'left',
      alignItems: 'left',
    },
    trailDuration: {
      paddingHorizontal: 3,
      fontSize: 12,
      color : 'black',
      fontWeight: '3~400'
    }
});


export default Emergency