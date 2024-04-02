// Generated by view binder compiler. Do not edit!
package com.example.simpleboggle.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.simpleboggle.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentMenuBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button newGameButton;

  @NonNull
  public final TextView scoreNum;

  @NonNull
  public final TextView scoreText;

  private FragmentMenuBinding(@NonNull ConstraintLayout rootView, @NonNull Button newGameButton,
      @NonNull TextView scoreNum, @NonNull TextView scoreText) {
    this.rootView = rootView;
    this.newGameButton = newGameButton;
    this.scoreNum = scoreNum;
    this.scoreText = scoreText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentMenuBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentMenuBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_menu, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentMenuBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.newGameButton;
      Button newGameButton = ViewBindings.findChildViewById(rootView, id);
      if (newGameButton == null) {
        break missingId;
      }

      id = R.id.scoreNum;
      TextView scoreNum = ViewBindings.findChildViewById(rootView, id);
      if (scoreNum == null) {
        break missingId;
      }

      id = R.id.scoreText;
      TextView scoreText = ViewBindings.findChildViewById(rootView, id);
      if (scoreText == null) {
        break missingId;
      }

      return new FragmentMenuBinding((ConstraintLayout) rootView, newGameButton, scoreNum,
          scoreText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
