package org.esa.snap.product.library.ui.v2;

import org.apache.commons.lang.StringUtils;
import org.esa.snap.remote.products.repository.Attribute;
import org.esa.snap.remote.products.repository.RepositoryProduct;
import org.esa.snap.ui.loading.SwingUtils;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by jcoravu on 23/9/2019.
 */
public class RepositoryProductPanel extends JPanel {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private static final DecimalFormat FORMAT = new DecimalFormat("###.##");

    private final JLabel nameLabel;
    private final JLabel quickLookImageLabel;
    private final JLabel firstAttributeLabel;
    private final JLabel acquisitionDateLabel;
    private final JLabel sizeLabel;
    private final JLabel urlLabel;
    private final JLabel secondAttributeLabel;
    private final JLabel missionLabel;
    private final JButton expandOrCollapseButton;
    private final ImageIcon expandImageIcon;
    private final ImageIcon collapseImageIcon;
    private final ComponentDimension componentDimension;
    private final RepositoryProductPanelBackground repositoryProductPanelBackground;

    protected final JLabel statusLabel;

    private JPanel attributesPanel;
    private RepositoryProduct repositoryProduct;

    public RepositoryProductPanel(RepositoryProductPanelBackground repositoryProductPanelBackground,
                                  ComponentDimension componentDimension, ImageIcon expandImageIcon, ImageIcon collapseImageIcon) {

        super(new BorderLayout(componentDimension.getGapBetweenColumns(), componentDimension.getGapBetweenRows()));

        this.repositoryProductPanelBackground = repositoryProductPanelBackground;
        this.componentDimension = componentDimension;
        this.expandImageIcon = expandImageIcon;
        this.collapseImageIcon = collapseImageIcon;

        this.nameLabel = new JLabel("");
        this.quickLookImageLabel = new JLabel(ProductListModel.EMPTY_ICON);
        this.firstAttributeLabel = new JLabel("");
        this.acquisitionDateLabel = new JLabel("");
        this.sizeLabel = new JLabel("");
        this.urlLabel = new JLabel("");
        this.secondAttributeLabel = new JLabel("");
        this.missionLabel = new JLabel("");
        this.statusLabel = new JLabel("");

        Dimension buttonSize = new Dimension(this.expandImageIcon.getIconWidth() + 2, this.expandImageIcon.getIconHeight() + 2);

        this.expandOrCollapseButton = new JButton(this.expandImageIcon);
        this.expandOrCollapseButton.setPreferredSize(buttonSize);
        this.expandOrCollapseButton.setMinimumSize(buttonSize);
        this.expandOrCollapseButton.setMaximumSize(buttonSize);
        this.expandOrCollapseButton.setFocusable(false);
        this.expandOrCollapseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (attributesPanel == null) {
                    addAttributesPanel(repositoryProduct);
                } else {
                    remoteAttributesPanel();
                }
            }
        });

        int gapBetweenRows = componentDimension.getGapBetweenRows();
        int gapBetweenColumns = componentDimension.getGapBetweenColumns();
        int number = 7;

        Border outsideBorder = new MatteBorder(0, 0, 1, 0, UIManager.getColor("controlShadow"));
        Border insideBorder = new EmptyBorder(gapBetweenRows, gapBetweenColumns, gapBetweenRows, gapBetweenColumns);
        setBorder(new CompoundBorder(outsideBorder, insideBorder));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints c = SwingUtils.buildConstraints(0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST, 3, 1, 0, 0);
        panel.add(this.urlLabel, c);
        c = SwingUtils.buildConstraints(3, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 1, 4, gapBetweenRows, 0);
        panel.add(new JLabel(""), c);

        c = SwingUtils.buildConstraints(0, 1, GridBagConstraints.NONE, GridBagConstraints.WEST, 1, 1, gapBetweenRows, 0);
        panel.add(this.missionLabel, c);
        c = SwingUtils.buildConstraints(1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST, 1, 1, gapBetweenRows, number * gapBetweenColumns);
        panel.add(this.firstAttributeLabel, c);
        c = SwingUtils.buildConstraints(2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 2, 1, gapBetweenRows, number * gapBetweenColumns);
        panel.add(this.secondAttributeLabel, c);

        c = SwingUtils.buildConstraints(0, 2, GridBagConstraints.NONE, GridBagConstraints.WEST, 1, 1, gapBetweenRows, 0);
        panel.add(this.acquisitionDateLabel, c);
        c = SwingUtils.buildConstraints(1, 2, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST, 3, 1, gapBetweenRows, gapBetweenColumns);
        panel.add(new JLabel(), c);

        c = SwingUtils.buildConstraints(0, 3, GridBagConstraints.NONE, GridBagConstraints.WEST, 1, 1, gapBetweenRows, 0);
        panel.add(this.sizeLabel, c);
        c = SwingUtils.buildConstraints(1, 3, GridBagConstraints.NONE, GridBagConstraints.WEST, 3, 1, gapBetweenRows, number * gapBetweenColumns);
        panel.add(this.statusLabel, c);

        JPanel expandCollapsePanel = new JPanel(new GridBagLayout());
        expandCollapsePanel.setOpaque(false);
        c = SwingUtils.buildConstraints(0, 0, GridBagConstraints.VERTICAL, GridBagConstraints.CENTER, 1, 1, 0, 0);
        expandCollapsePanel.add(new JLabel(), c);
        c = SwingUtils.buildConstraints(0, 1, GridBagConstraints.NONE, GridBagConstraints.SOUTH, 1, 1, 0, 0);
        expandCollapsePanel.add(this.expandOrCollapseButton, c);

        add(this.nameLabel, BorderLayout.NORTH);
        add(this.quickLookImageLabel, BorderLayout.WEST);
        add(panel, BorderLayout.CENTER);
        add(expandCollapsePanel, BorderLayout.EAST);
    }

    @Override
    public Color getBackground() {
        if (this.repositoryProductPanelBackground != null) {
            return this.repositoryProductPanelBackground.getProductPanelBackground(this);
        }
        return super.getBackground();
    }

    protected final Color getDefaultForegroundColor() {
        return this.sizeLabel.getForeground();
    }

    public void refresh(int index, ProductListModel productListModel) {
        if (this.attributesPanel != null) {
            if (this.repositoryProduct == null || this.repositoryProduct != productListModel.getProductAt(index)) {
                remoteAttributesPanel();
            }
        }

        repositoryProduct = productListModel.getProductAt(index);

        this.nameLabel.setText(repositoryProduct.getName());
        this.urlLabel.setText(buildAttributeLabelText("URL", repositoryProduct.getDownloadURL()));

        String mission = (StringUtils.isBlank(repositoryProduct.getMission()) ? "N/A" : repositoryProduct.getMission());
        this.missionLabel.setText(buildAttributeLabelText("Mission", mission));

        Map<String, String> visibleAttributes = productListModel.getMissionVisibleAttributes(repositoryProduct.getMission());
        updateVisibleAttributes(repositoryProduct, visibleAttributes);

        ImageIcon imageIcon = productListModel.getProductQuickLookImage(repositoryProduct);
        this.quickLookImageLabel.setIcon(imageIcon);

        String dateAsString = "N/A";
        if (repositoryProduct.getAcquisitionDate() != null) {
            dateAsString = DATE_FORMAT.format(repositoryProduct.getAcquisitionDate());
        }
        this.acquisitionDateLabel.setText(buildAttributeLabelText("Acquisition date", dateAsString));

        updateSize(repositoryProduct);
    }

    private void updateSize(RepositoryProduct repositoryProduct) {
        String sizeText = "Size: ";
        if (repositoryProduct.getApproximateSize() > 0) {
            float oneKyloByte = 1024.0f;
            double sizeInMegaBytes = repositoryProduct.getApproximateSize() / (oneKyloByte * oneKyloByte);
            if (sizeInMegaBytes > oneKyloByte) {
                double sizeInGigaBytes = sizeInMegaBytes / oneKyloByte;
                sizeText += FORMAT.format(sizeInGigaBytes) + " GB";
            } else {
                sizeText += FORMAT.format(sizeInMegaBytes) + " MB";
            }
        } else {
            sizeText += "N/A";
        }
        this.sizeLabel.setText(sizeText);
    }

    private void addAttributesPanel(RepositoryProduct repositoryProduct) {
        int gapBetweenRows = 5;
        int gapBetweenColumns = 5;

        attributesPanel = new JPanel(new BorderLayout(gapBetweenColumns, gapBetweenRows));
        attributesPanel.setOpaque(false);
        attributesPanel.add(new JLabel("Attributes"), BorderLayout.NORTH);

        int columnCount = 3;
        int rowCount = repositoryProduct.getAttributes().size() / columnCount;
        if (repositoryProduct.getAttributes().size() % columnCount != 0) {
            rowCount++;
        }
        JPanel panel = new JPanel(new GridLayout(rowCount, columnCount, gapBetweenColumns, gapBetweenRows));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 5 * gapBetweenColumns, 0, 0));
        for (int i=0; i<repositoryProduct.getAttributes().size(); i++) {
            Attribute attribute = repositoryProduct.getAttributes().get(i);
            JLabel label = new JLabel(attribute.getName() + ": " + attribute.getValue());
            panel.add(label);
        }
        attributesPanel.add(panel, BorderLayout.CENTER);

        add(attributesPanel, BorderLayout.SOUTH);

        this.expandOrCollapseButton.setIcon(this.collapseImageIcon);
        revalidate();
        repaint();
    }

    private void remoteAttributesPanel() {
        remove(this.attributesPanel);
        this.expandOrCollapseButton.setIcon(this.expandImageIcon);
        this.attributesPanel = null;
        revalidate();
        repaint();
    }

    private void updateVisibleAttributes(RepositoryProduct repositoryProduct, Map<String, String> visibleAttributes) {
        String firstLabelText = "";
        String secondLabelText = "";
        List<Attribute> attributes = repositoryProduct.getAttributes();
        if (visibleAttributes != null && visibleAttributes.size() > 0 && attributes != null && attributes.size() > 0) {
            for (Map.Entry<String, String> entry : visibleAttributes.entrySet()) {
                String attributeName = entry.getKey();
                String attributeDisplayName = entry.getValue();
                Attribute foundAttribute = null;
                for (int i = 0; i < attributes.size() && foundAttribute == null; i++) {
                    Attribute attribute = attributes.get(i);
                    if (attributeName.equals(attribute.getName())) {
                        foundAttribute = attribute;
                    }
                }
                if (foundAttribute != null) {
                    if (firstLabelText.length() == 0) {
                        firstLabelText = buildAttributeLabelText(attributeDisplayName, foundAttribute.getValue());
                    } else if (secondLabelText.length() == 0) {
                        secondLabelText = buildAttributeLabelText(attributeDisplayName, foundAttribute.getValue());
                        break;
                    }
                }
            }
        }
        this.firstAttributeLabel.setText(firstLabelText);
        this.secondAttributeLabel.setText(secondLabelText);
    }

    private static String buildAttributeLabelText(String attributeDisplayName, String attributeValue) {
        return attributeDisplayName + ": " + attributeValue;
    }
}
