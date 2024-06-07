import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList, ActivityIndicator, TouchableOpacity, Linking } from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'

/* TO-DO:

- Melhorar interface

*/

const Emergency = () => {
    const [contacts, setContact] = useState("Loading");


    const getContact = async () => {
      try {
        const response = await fetch(api + 'app');
        if (response.ok) {
          const data = await response.json();
          setContact(data[0].contacts);
          //console.log(data[0].contacts);

        } else {
            setContact("Error fetching contacts");
        }
      } catch (error) {
        setContact("Error fetching data");
      }
    };


    useEffect(() => {
        getContact();
    }, []);
    // <Image source={{ uri: item.image }} style={styles.trailImage} />

    const renderPhone = ({ item }) => (
        <View>
        <TouchableOpacity onPress={() => Linking.openURL(`tel:${item.contact_phone}`)}>
          <Text style={styles.phoneText}>{item.contact_phone}</Text>
        </TouchableOpacity>
        <View style={styles.separator} />
      </View>

    );

    const renderEmail= ({ item }) => (
        <View style={styles.container}>
        <TouchableOpacity onPress={() => Linking.openURL(`mailto:${item.contact_mail}`)}>
          <Text style={styles.phoneText}>{item.contact_mail}</Text>
        </TouchableOpacity>
      </View>
    );


  
    return (

      <View style={styles.container}>
        <Text style={styles.title}>Phones</Text>
        <FlatList
        data={contacts}
        renderItem={renderPhone}
        keyExtractor={(item) => item.contact_phone}
        vertical
        showsHorizontalScrollIndicator={false}
      />
        

    <Text style={styles.title}>Email</Text>
    <FlatList
        data={contacts}
        renderItem={renderEmail}
        keyExtractor={(item) => item.contact_mail}
        vertical
        showsHorizontalScrollIndicator={false}
      />
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
      marginBottom: 55,
      color : 'black'
    },
    trailItem: {
        marginRight: 16,
        alignItems: 'center',
      },
      trailImage: {
        width: 100,
        height: 100,
        borderRadius: 8,
      },
      trailName: {
        marginTop: 8,
        fontSize: 16,
        color : 'black',
        fontWeight: 'bold',
      }, 
      phoneText: {
        marginTop: 8,
        fontSize: 14,
        color: 'blue',
        textDecorationLine: 'underline',
      },
      separator: {
        height: 1,
        backgroundColor: '#CCCCCC',  // or any color you prefer
        marginVertical: 8,  // adjust the spacing as needed
      },
  });


export default Emergency