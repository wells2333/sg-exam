export const setVideoSrc = (subject, $refs, timeout = 50) => {
  if (subject.subjectVideoUrl) {
    setTimeout(() => {
      if ($refs.sgVideo !== undefined) {
        $refs.sgVideo.setSrc(subject.subjectVideoUrl)
      }
    }, timeout)
  }
}

export const pauseVideo = ($refs) => {
  if ($refs.sgVideo !== undefined) {
    $refs.sgVideo.pause()
  }
}

export const setAudioSrc = (subject, $refs, autoPlay, timeout = 50) => {
  if (subject.speechUrl) {
    setTimeout(() => {
      if ($refs.sgAudio !== undefined) {
        $refs.sgAudio.setSrc(subject.speechUrl, autoPlay, subject.id)
      }
    }, timeout)
  }
}

export const pauseAudio = ($refs) => {
  if ($refs.sgAudio !== undefined) {
    $refs.sgAudio.pausePlay()
  }
}

export const getSubjectRef = ($refs, index, item) => {
  let ref
  switch (item.type) {
    case 0:
      ref = $refs['choices_' + index]
      break
    case 1:
      ref = $refs['shortAnswer_' + index]
      break
    case 2:
      ref = $refs['judgement_' + index]
      break
    case 3:
      ref = $refs['multipleChoices_' + index]
      break
    case 4:
      ref = $refs['fillBlank_' + index]
      break
    case 5:
      ref = $refs['material_' + index]
      break
    case 6:
      ref = $refs['sVideo_' + index]
      break
  }
  return ref[0]
}

export const setSubjectInfo = ($refs, index, item, subjects) => {
  const ref = getSubjectRef($refs, index, item)
  if (ref) {
    try {
      ref.setSubjectInfo(item, subjects.length, null)
    } catch (error) {
      console.error(error)
    }
  }
}

export const beforeSaveSubject = ($refs, subjects) => {
  for (let i = 0; i < subjects.length; i++) {
    const ref = getSubjectRef($refs, i, subjects[i])
    if (ref) {
      ref.beforeSave()
    }
  }
}

export const replaceFirstP = (str, type, sort) => {
  if (str.indexOf('<p') !== -1) {
    return str.replace('<p>', `<p><span style="color: #999; margin-right: 2px;">[${type}]&nbsp;</span>${sort}.`)
  }
  return `<span style="color: #999; margin-right: 2px;">[${type}]&nbsp;</span>${sort}.` + str
}
