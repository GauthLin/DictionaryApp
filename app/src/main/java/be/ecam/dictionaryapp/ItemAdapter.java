package be.ecam.dictionaryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

import be.ecam.dictionaryapp.Entity.Translation;
import be.ecam.dictionaryapp.Entity.Word;

/**
 * Created by damien on 21.02.17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder> {
    //aura deux classes, une pour creer et une autre pour peupler (et recycler du coup)

    private Cursor cursor;
    private List<Word> vocabList = MainActivity.getVocabulary();
    private Word myTranslationObject;

    private ItemAdapterOnClickHandler clickHandler;

    //constructeur de ItemAdapter
    public ItemAdapter(ItemAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public interface ItemAdapterOnClickHandler {
        void onClick(int index);
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // on va creer une viewHolder, un bloc contenant une vue.
        // mission = garder les references vers les vues qui doivent etre mises à jour.

        public final TextView trans_origin_TextView;
        public final TextView trans_translation_TextView;

        public ItemAdapterViewHolder(View view) {
            super(view);
            trans_origin_TextView = (TextView) view.findViewById(R.id.trans_origin);
            trans_translation_TextView = (TextView) view.findViewById(R.id.trans_translation);
            view.setOnClickListener(this);
            // après avoir findviewbyId sur toutes les vues, on leur attahce chacune un
            // event listenenr, via le view.SETonclickListenener. donc, à la notre bloc 'view' on va
            // rajouter le listener.
        }
        //!! on a pas mis de VALEUR dans ces champs, on a juste lié avec les instances de blocs TextView
        // dans notre layout. Pour les peupler, on passera par onBindViewHolder

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            myTranslationObject = vocabList.get(adapterPosition);

            clickHandler.onClick(adapterPosition);
        }
    }

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // nouvelle méthode de itemAdapter, celle qui va CREER les viewHolders.

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.traduction_layout; // il a besoin de creer une des vues sur base du xml prédiction
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToPArentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToPArentImmediately);
        return new ItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int position) {
        myTranslationObject = vocabList.get(position);
        String textOrigin = myTranslationObject.getName();
        String textTrad = "";

        for (Translation translation :
                myTranslationObject.getTranslations()) {
            // Add dash between two translations
            if (!textTrad.isEmpty()) {
                textTrad += " - ";
            }
            textTrad += String.format("%s (%s)", translation.getTranslation(), translation.getLanguage());
        }

        itemAdapterViewHolder.trans_origin_TextView.setText(textOrigin);
        itemAdapterViewHolder.trans_translation_TextView.setText(textTrad);

        // si on a bien fait les choses: la "position" qui correspond à la postition de la view dans la recyclerview coincidera avec la position du
        // curseur dans le curseur.
        // on utilisera le curseur pour remplir les données dans le onbind view holder
        // move to position etc..
    }

    public void setData(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
        // le notify force le rafraichissement de l'affichage.
        // ca va forcer de relancer onBindViewHolder! comme si on rappelait la fonction.
    }
    // rajouter une fonction setData, elle recoit un curseur en param et la sauve dans une variable d'instance courseur ou mCursor..
    // dans la main activity, une fonction loadData/Database qui fait la query, et qui avec le curseur recu utilisera.

    @Override
    public int getItemCount() {
        return vocabList.size();
        // return 20;
    }
}
