import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'
import MapView, { Marker, Polyline } from 'react-native-maps';

/* !!!
NOTA: ALVIM MUda me isto sff
Neste momento so estou a fazer o pedido de um trail especifico. Ora esta pagina vai servir para todos os trails. É uma abstração.

Ou seja mudar:

Em vez de pedir um trail especifico, recebe um argumento de um trail e expoe esse trail

*/

const MapComponent = ({ data }) => {
    if (!data || data.length === 0) {
      return <Text>No map data available</Text>;
    }

  return (
    <MapView
      style={styles.map}
      initialRegion={{
        latitude: 41.55008,
        longitude: -8.4268,
        latitudeDelta: 0.1,
        longitudeDelta: 0.1,
      }}
    >
      {data.map((edge, index) => {
        if (!edge || !edge.edge_start || !edge.edge_end) return null;

        const start = edge.edge_start;
        const end = edge.edge_end;
        
        return (
          <View key={index}>
            <Marker
              coordinate={{ latitude: start.pin_lat, longitude: start.pin_lng }}
              title={start.pin_name}
              description={start.pin_desc}
            />
            <Marker
              coordinate={{ latitude: end.pin_lat, longitude: end.pin_lng }}
              title={end.pin_name}
              description={end.pin_desc}
            />
            <Polyline
              coordinates={[
                { latitude: start.pin_lat, longitude: start.pin_lng },
                { latitude: end.pin_lat, longitude: end.pin_lng }
              ]}
              strokeColor="#000"
              strokeWidth={3}
            />
          </View>
        );
      })}
    </MapView>
  );
};


const Trail = () => {

    const dispatch = useDispatch();
    const trail_id = '1';

    //const GeoDistance = useSelector((state) => state.distance.distanceVal);

    //console.log(GeoDistance);
  
    const [trail, setTrail] = useState("Loading!");

    const getTrail = async (trail_id) => {
      try {
        const response = await fetch(api + 'trail/' + trail_id );
        if (response.ok) {
          const data = await response.json();
          setTrail(data);
          console.log(trail);
        } else {
          setTrail("Error fetching trail id=" + trail_id);
        }
      } catch (error) {
        setAppInfo("Error! Api Down?");
      }
    };

    useEffect(() => {
      getTrail(trail_id); // NOTA O QUE TEMOS A MUDAR É AQUI E MUDAMOS O TRAIL PARA RECEBER ARGUMENTOS!
    }, []);

    const [startTime, setStartTime] = useState(null);
    const [isToggled, setToggle] = useState(false);
    const [visitedTrips, setVisitedTrips] = useState([]);
    const [notifications, setNotifications] = useState([]);

    const handleButtonToggle = () => {
      setToggle(!isToggled);
    };

    data = trail.edges;

    
  return (

    <View style={styles.container}>
        <Text style={styles.title}>{trail.trail_name}</Text>
        <Text style={styles.title}>{trail.trail_desc}</Text>
        <Text style={styles.title}>{trail.trail_duration} min</Text>
        <Text style={styles.title}>Difficulty : {trail.trail_difficulty}</Text>

        {trail.trail_img ? (
            <Image 
              source={{ uri: trail.trail_img }} 
              style={styles.trailImage}
              onError={() => console.log('Error loading image:', trail.trail_img)} 
            />
          )  :(
            <Text style={styles.title}>No image available</Text>
          )}

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
    fontSize: 15,
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