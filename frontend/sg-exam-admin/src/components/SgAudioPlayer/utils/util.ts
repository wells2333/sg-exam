export const formatSecond = (second: number) => {
  let hour_str = `${Math.floor(second / 60)}`
  let second_str = `${Math.ceil(second % 60)}`
  if (hour_str.length === 1) {
    hour_str = `0${hour_str}`
  }
  if (second_str.length === 1) {
    second_str = `0${second_str}`
  }
  return `${hour_str}:${second_str}`
}
