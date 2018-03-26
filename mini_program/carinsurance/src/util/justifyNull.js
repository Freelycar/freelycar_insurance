export default function justifyNull(obj) {
  for (let key in obj) {
    if (!obj[key]) {
      obj[key] = '';
    }
  }
}
