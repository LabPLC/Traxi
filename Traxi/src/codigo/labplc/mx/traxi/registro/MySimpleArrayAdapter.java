package codigo.labplc.mx.traxi.registro;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  List<String> values;

  public MySimpleArrayAdapter(Context context, List<String> values) {
    super(context, R.layout.row_lista, values);
    this.context = context;
    this.values=values;
  }
  


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row_lista, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.row_lista_tv_info);
    textView.setText(values.get(position));
    textView.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
    textView.setTextColor(context.getResources().getColor(R.color.color_vivos));

    return rowView;
  }
} 