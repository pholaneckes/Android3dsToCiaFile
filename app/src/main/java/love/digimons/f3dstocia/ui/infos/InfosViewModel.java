package love.digimons.f3dstocia.ui.infos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InfosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("算法：https://github.com/ihaveamac\n移植：pholaneckes");
    }

    public LiveData<String> getText() {
        return mText;
    }
}