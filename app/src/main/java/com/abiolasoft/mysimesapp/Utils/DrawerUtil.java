package com.abiolasoft.mysimesapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.abiolasoft.mysimesapp.Activities.AccountSettingsActivity;
import com.abiolasoft.mysimesapp.Activities.ELibraryActivity;
import com.abiolasoft.mysimesapp.Activities.HomeActivity;
import com.abiolasoft.mysimesapp.Activities.TimetableDayActivity;
import com.abiolasoft.mysimesapp.Activities.UploadMultipleToLibraryActivity;
import com.abiolasoft.mysimesapp.Activities.UploadToLibraryActivity;
import com.abiolasoft.mysimesapp.Models.UserDetails;
import com.abiolasoft.mysimesapp.R;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.squareup.picasso.Picasso;

public class DrawerUtil {


    public void getDrawer(final Activity activity, Toolbar toolbar, UserDetails currentUserDetails) {

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                super.set(imageView, uri, placeholder);

                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                super.cancel(imageView);
            }



            @Override
            public Drawable placeholder(Context ctx, String tag) {

                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(new ProfileDrawerItem()
                        .withName(currentUserDetails.getDisplayName())
                        .withEmail(currentUserDetails.getEmail())
                        .withIcon(currentUserDetails.getImage_thumb())
                ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        Intent accountIntent = new Intent(activity, AccountSettingsActivity.class);
                        activity.startActivity(accountIntent);
                        return true;
                    }
                }).build();


        //if you want to update the items at a later time it is recommended to keep it in a variable
        //PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("MySimesMessenger").withIdentifier(1);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("My Forum").withIdentifier(1);


        ExpandableDrawerItem item3 = new ExpandableDrawerItem().withName("My timetable").withIdentifier(2);

        ExpandableDrawerItem item4 = new ExpandableDrawerItem().withName("My  E-Library").withSubItems(
                new SecondaryDrawerItem().withName("ELibrary").withIdentifier(3),
                new SecondaryDrawerItem().withName("Upload a File").withIdentifier(4),
                new SecondaryDrawerItem().withName("Upload Multiple Files").withIdentifier(5)

        );

        ExpandableDrawerItem item5 = new ExpandableDrawerItem().withName("Executives").withIdentifier(6);


        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withName("Logout").withIdentifier(7);


        Drawer result = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(activity)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)

                .addDrawerItems(
                        item1, item3, item4, item5, item6

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Intent drawerItemIntent = null;
                        long identifier = drawerItem.getIdentifier();

                        if (drawerItem != null) {
                            if (identifier == 4) {
                                drawerItemIntent = new Intent(activity, UploadToLibraryActivity.class);
                            } else if (identifier == 3) {
                                drawerItemIntent = new Intent(activity, ELibraryActivity.class);
                            } else if (identifier == 2) {
                                drawerItemIntent = new Intent(activity, TimetableDayActivity.class);
                            } else if (identifier == 5) {
                                drawerItemIntent = new Intent(activity, UploadMultipleToLibraryActivity.class);
                            } else if (identifier == 1) {
                                drawerItemIntent = new Intent(activity, HomeActivity.class);
                            }
                        }

                        if (drawerItemIntent != null) {
                            activity.startActivity(drawerItemIntent);
                        }

                        return false;

                        /*if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(activity, SignInActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(activity, MySimesBlog.class);
                            } else if (drawerItem.getIdentifier() == 31) {
                                intent = new Intent(activity, TimeTableWeek.class);
                            } else if (drawerItem.getIdentifier() == 32) {
                                intent = new Intent(activity, EditTimetable.class);
                            } else if (drawerItem.getIdentifier() == 33) {
                                intent = new Intent(activity, AddCourse.class);
                            }else if (drawerItem.getIdentifier() == 41) {
                                intent = new Intent(activity, Elibrary.class);
                            } else if (drawerItem.getIdentifier() == 42) {
                                intent = new Intent(activity, UploadToLibraryActivity.class);
                            } else if(drawerItem.getIdentifier() == 43){
                                intent = new Intent(activity, MultipleUploads.class);
                            } else if (drawerItem.getIdentifier() == 51) {
                                intent = new Intent(activity, MyExecutives.class);
                            } else if (drawerItem.getIdentifier() == 52) {
                                intent = new Intent(activity, JoinExecutives.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                FirebaseAuth.getInstance().signOut();
                                intent = activity.getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                *//*intent = new Intent(activity, StartActivity.class);
                                activity.finish();*//*
                            } *//* else if (drawerItem.getIdentifier() == 15) {
                                intent = new Intent(activity, CrossfadeDrawerLayoutActvitiy.class);
                            }*//* else if (drawerItem.getIdentifier() == 20) {
                                *//*intent = new LibsBuilder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .intent(activity);*//*
                            }
                            if (intent != null) {
                                activity.startActivity(intent);
                            }
                        }*/


                    }
                })
                .build();
        /*headerResult.updateProfile(new ProfileDrawerItem()
                .withIcon(R.drawable.com_facebook_profile_picture_blank_square)
                .withName(currentUserDetails.getDisplayName())
                .withEmail(currentUserDetails.getEmail()));*/
    }

}
