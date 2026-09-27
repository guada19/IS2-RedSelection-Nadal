export const traducirDescripcion = (descripcionEnIngles) => {

    const traducciones = {
      'clear sky': 'cielo despejado',
      'few clouds': 'pocas nubes',
      'scattered clouds': 'nubes dispersas',
      'broken clouds': 'nubes rotas',
      'overcast clouds': 'nublado',
      'shower rain': 'lluvia',
      'rain': 'lluvia',
      'thunderstorm': 'tormenta eléctrica',
      'snow': 'nieve',
      'mist': 'niebla',
      'smoke': 'humo',
      'haze': 'neblina',
      'dust': 'polvo',
      'fog': 'niebla',
      'sand': 'arena',
      'ash': 'ceniza',
      'squall': 'ráfaga',
      'tornado': 'tornado'
    };
  
    return traducciones[descripcionEnIngles] || descripcionEnIngles;
  };