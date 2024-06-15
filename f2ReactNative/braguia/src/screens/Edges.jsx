import { View, Text, Image, TouchableOpacity  } from 'react-native';
import { StyleSheet, FlatList , ScrollView, Linking} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import { useNavigation } from '@react-navigation/native';
import {cores, api, api2} from '../var.js'
import MapView, { Marker, Polyline } from 'react-native-maps';
import Icon from 'react-native-vector-icons/MaterialIcons';


const Edges = ({ route }) => {
    const { trail_id } = route.params;

    const [trail, setTrail] = useState("Loading!");

    const trails  = useSelector(state => state.data.appData.trails);

    const navigation = useNavigation();


    const getTrail = async (trail_id) => {
      

        setTrail(trails[trail_id-1]);
      };
  
      useEffect(() => {
        getTrail(trail_id);
      }, [trail_id, trails]);
    
  
      const [startTime, setStartTime] = useState(null);
      const [isToggled, setToggle] = useState(false);
      const [visitedTrips, setVisitedTrips] = useState([]);
      const [notifications, setNotifications] = useState([]);
  
      const handleButtonToggle = () => {
        setToggle(!isToggled);
      };
  
      const transformedData = trail && trail.edges && trail.edges.length > 0 
  ? [trail.edges[0].edge_start, ...trail.edges.map(edge => edge.edge_end)] 
  : [];

      const renderEdge = ({ item }) => (
        <TouchableOpacity style={styles.edgeItem} onPress={() => navigation.navigate('Pin', { pin_id: item.id })}>
          <Text style={styles.edgeName}>{item.pin_name}</Text>
          {item.media.length > 0 && item.media[0].media_file &&
            <Image
              source={{ uri: item.media[0].media_file }}
              style={styles.edgeImage}
            />}
        </TouchableOpacity>
      );

    return (
          <View style={styles.container}>

    
                
                <FlatList
                  data={transformedData}
                  renderItem={renderEdge}
                  keyExtractor={(item) => item.id.toString()}
                  showsVerticalScrollIndicator={false}
                  style={styles.list}
                  contentContainerStyle={styles.listContent}
              />
          </View>
      );
}

const styles = StyleSheet.create({
      container: {
        flex: 1,
        backgroundColor : 'white',
        //marginBottom: 50
      },
      list: {
        flex: 1,
        width: '100%',
    },
    listContent: {
        alignItems: 'center',
        paddingBottom:60
    },
      edgeItem: {
          height: 150,
          width: 300,
          flexDirection: 'row',
          marginBottom: 0,
          alignItems: 'center',
          //borderColor: 'gray',
          borderWidth: 0.5,
          borderTopColor: 'gray',
          borderBottomColor: 'gray',
          borderRightColor: 'white',
          borderLeftColor: 'white',
        },
        edgeImage: {
          width: 100,
          height: 100,
          borderRadius: 8,
        },
        edgeName: {
          paddingHorizontal: 15,
          marginTop: 8,
          fontSize: 16,
          color : 'black',
          fontWeight: 'bold',
        }
    });

export default Edges