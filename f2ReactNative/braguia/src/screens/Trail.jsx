import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'
import MapView, { Marker, Polyline } from 'react-native-maps';
import MapComponent from '../components/MapComponent.js'

/* !!!
NOTA: ALVIM MUda me isto sff
Neste momento so estou a fazer o pedido de um trail especifico. Ora esta pagina vai servir para todos os trails. É uma abstração.

Ou seja mudar:

Em vez de pedir um trail especifico, recebe um argumento de um trail e expoe esse trail

*/




const Trail = () => {

    const [trail, setTrail] = useState("Loading!");

    const getTrail = async (trail_id) => {
      //console.log("Fetching trail id=" + trail_id);
      //console.log(api + 'trails?='+ trail_id);
      try {
        const response = await fetch(api + 'trails?id=' + trail_id);
        
        if (response.ok) {
          const data = await response.json();

          //trail2 = data[0];
          //console.log(trail2);
          //console.log("NOME" ,trail2.trail_name);
          //console.log("IMG", trail2.trail_img)
          //console.log("dur", trail2.trail_duration)
          
          setTrail(data[0]);
          //console.log("ESTOU AQUI")
          //console.log("DATA HERE",data[0]);
          
          
        } else {
          setTrail("Error fetching trail id=" + trail_id);
        }
      } catch (error) {
        setAppInfo("Error! Api Down?");
      }
    };

    useEffect(() => {
      getTrail('1'); // NOTA O QUE TEMOS A MUDAR É AQUI E MUDAMOS O TRAIL PARA RECEBER ARGUMENTOS! 
    }, []);

    data = trail.edges;

  return (

    <View style={styles.container}>
        <Text style={styles.title}>{trail.trail_name}</Text>
        <Text style={styles.title}>{trail.trail_desc}</Text>
        <Text style={styles.title}>{trail.trail_duration} min </Text>
        <Text style={styles.title}>Difficulty : {trail.trail_difficulty}</Text>

        <Image source={{ uri: trail.trail_img }} style={styles.trailImage}
        onError={console.log(trail.trail_img)} />
        <Text style={styles.title}>DA ERRO DISPLAY IMAGE</Text>

        <MapComponent data={data} />
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
    //marginBottom: 55,
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
    map: {
      width: '100%',
      height: 150, // Adjust the height here to make the map smaller
    },
});

export default Trail;