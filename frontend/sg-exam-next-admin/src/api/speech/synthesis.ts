import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {SpeechApi} from "/@/api/api";

export const getSpeechSynthesisList = (params?: object) =>
  defHttp.get<ApiRes>({url: SpeechApi.SynthesisList, params});

export const createSpeech = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: SpeechApi.Base,
      params,
    }
  );
};

export const updateSpeech = (id: string, params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: SpeechApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteSpeech = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: SpeechApi.Base + '/' + id,
    }
  );
};


